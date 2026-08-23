package com.hr.demo.hr.service.impl;

import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.domain.user.Role;
import com.hr.demo.hr.dto.AddCompanyUserRequest;
import com.hr.demo.hr.dto.UpdateCompanyUserRequest;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.exceptions.UserAlreadyExistsException;
import com.hr.demo.hr.entity.*;
import com.hr.demo.hr.repository.*;
import com.hr.demo.reaponse.AssignedAssetResponse;
import com.hr.demo.reaponse.UserResponse;
import com.hr.demo.hr.service.FileStorageService;
import com.hr.demo.hr.service.UserService;
import com.hr.demo.hr.service.faceverify.FaceRecognitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Set<Role> ROLES_COMPANY_ADMIN_CAN_CREATE =
            EnumSet.of(Role.HR, Role.EMPLOYEE);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final RoleRepository roleRepository;
    private final AssetRepository assetRepository;
    private final FileStorageService fileStorageService;
    private final FaceRecognitionService faceRecognitionService;

    private UserEntity getCurrentAdmin() {
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UnauthorizedException("Authenticated user not found"));
        if (admin.getRole() != Role.COMPANY_ADMIN) {
            throw new UnauthorizedException("Only company admin can manage users");
        }
        return admin;
    }

    private CompanyEntity getAdminCompany(UserEntity admin) {
        CompanyEntity company = admin.getCompany();
        if (company == null) {
            throw new UnauthorizedException("Company admin is not linked to a company");
        }
        if (company.getStatus() != CompanyStatus.ACTIVE) {
            throw new UnauthorizedException("Company is not active");
        }
        return company;
    }

    private void checkEmployeeLimit(CompanyEntity company) {
        Integer limit = company.getEmployeeLimit();
        if (limit != null && limit > 0) {
            long current = userRepository.countByCompany_Id(company.getId());
            if (current >= limit) {
                throw new UnauthorizedException("Employee limit reached for this company");
            }
        }
    }

    @Override
    public UserResponse addCompanyUser(AddCompanyUserRequest request) {
        UserEntity admin = getCurrentAdmin();
        CompanyEntity company = getAdminCompany(admin);

        checkEmployeeLimit(company);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with this email");
        }

        Role role = Role.from(request.getRole());
        if (!ROLES_COMPANY_ADMIN_CAN_CREATE.contains(role)) {
            throw new UnauthorizedException("Company admin can only create HR or EMPLOYEE users");
        }

        DepartmentEntity department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findByIdAndCompany_Id(request.getDepartmentId(), company.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        }

        DesignationEntity designation = null;
        if (request.getDesignationId() != null) {
            designation = designationRepository.findByIdAndCompany_Id(request.getDesignationId(), company.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Designation not found"));
        }

        RoleEntity userRole = null;
        if (request.getRoleId() != null) {
            userRole = roleRepository.findByIdAndCompany_Id(request.getRoleId(), company.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        }

        UserEntity user = UserEntity.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .employeeId(request.getEmployeeId())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .maritalStatus(request.getMaritalStatus())
                .nationality(request.getNationality())
                .bloodGroup(request.getBloodGroup())
                .panNumber(request.getPanNumber())
                .aadhaarNumber(request.getAadhaarNumber())
                .passportNumber(request.getPassportNumber())
                .profilePhoto(request.getProfilePhoto())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .department(department)
                .designation(designation)
                .userRole(userRole)
                .role(role)
                .company(company)
                .employmentType(request.getEmploymentType())
                .dateOfJoining(request.getDateOfJoining())
                .reportingManager(request.getReportingManager())
                .workLocation(request.getWorkLocation())
                .probationPeriod(request.getProbationPeriod())
                .confirmationDate(request.getConfirmationDate())
                .shift(request.getShift())
                .employeeCategory(request.getEmployeeCategory())
                .costCenter(request.getCostCenter())
                .businessUnit(request.getBusinessUnit())
                .officialEmail(request.getOfficialEmail())
                .officialPhone(request.getOfficialPhone())
                .personalEmail(request.getPersonalEmail())
                .personalPhone(request.getPersonalPhone())
                .currentAddress(request.getCurrentAddress())
                .permanentAddress(request.getPermanentAddress())
                .paySchedule(request.getPaySchedule())
                .currency(request.getCurrency())
                .basicSalary(request.getBasicSalary())
                .grossSalary(request.getGrossSalary())
                .paymentFrequency(request.getPaymentFrequency())
                .bankName(request.getBankName())
                .bankAccountNumber(request.getBankAccountNumber())
                .ifscCode(request.getIfscCode())
                .pfNumber(request.getPfNumber())
                .esiNumber(request.getEsiNumber())
                .uanNumber(request.getUanNumber())
                .taxRegime(request.getTaxRegime())
                .education(request.getEducation())
                .experienceYears(request.getExperienceYears())
                .skills(request.getSkills())
                .languagesKnown(request.getLanguagesKnown())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactRelationship(request.getEmergencyContactRelationship())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .build();

        UserEntity savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    @Override
    public UserResponse updateCompanyUser(Long id, UpdateCompanyUserRequest request) {
        UserEntity admin = getCurrentAdmin();
        CompanyEntity company = getAdminCompany(admin);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getCompany().getId().equals(company.getId())) {
            throw new UnauthorizedException("User does not belong to your company");
        }

        Role role = Role.from(request.getRole());
        if (!ROLES_COMPANY_ADMIN_CAN_CREATE.contains(role)) {
            throw new UnauthorizedException("Company admin can only assign HR or EMPLOYEE roles");
        }

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        DepartmentEntity department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findByIdAndCompany_Id(request.getDepartmentId(), company.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        }

        DesignationEntity designation = null;
        if (request.getDesignationId() != null) {
            designation = designationRepository.findByIdAndCompany_Id(request.getDesignationId(), company.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Designation not found"));
        }

        RoleEntity userRole = null;
        if (request.getRoleId() != null) {
            userRole = roleRepository.findByIdAndCompany_Id(request.getRoleId(), company.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        }

        user.setFirstName(request.getFirstName());
        user.setMiddleName(request.getMiddleName());
        user.setLastName(request.getLastName());
        user.setEmployeeId(request.getEmployeeId());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setMaritalStatus(request.getMaritalStatus());
        user.setNationality(request.getNationality());
        user.setBloodGroup(request.getBloodGroup());
        user.setPanNumber(request.getPanNumber());
        user.setAadhaarNumber(request.getAadhaarNumber());
        user.setPassportNumber(request.getPassportNumber());
        user.setProfilePhoto(request.getProfilePhoto());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDepartment(department);
        user.setDesignation(designation);
        user.setUserRole(userRole);
        user.setRole(role);

        user.setEmploymentType(request.getEmploymentType());
        user.setDateOfJoining(request.getDateOfJoining());
        user.setReportingManager(request.getReportingManager());
        user.setWorkLocation(request.getWorkLocation());
        user.setProbationPeriod(request.getProbationPeriod());
        user.setConfirmationDate(request.getConfirmationDate());
        user.setShift(request.getShift());
        user.setEmployeeCategory(request.getEmployeeCategory());
        user.setCostCenter(request.getCostCenter());
        user.setBusinessUnit(request.getBusinessUnit());

        user.setOfficialEmail(request.getOfficialEmail());
        user.setOfficialPhone(request.getOfficialPhone());
        user.setPersonalEmail(request.getPersonalEmail());
        user.setPersonalPhone(request.getPersonalPhone());
        user.setCurrentAddress(request.getCurrentAddress());
        user.setPermanentAddress(request.getPermanentAddress());

        user.setPaySchedule(request.getPaySchedule());
        user.setCurrency(request.getCurrency());
        user.setBasicSalary(request.getBasicSalary());
        user.setGrossSalary(request.getGrossSalary());
        user.setPaymentFrequency(request.getPaymentFrequency());
        user.setBankName(request.getBankName());
        user.setBankAccountNumber(request.getBankAccountNumber());
        user.setIfscCode(request.getIfscCode());
        user.setPfNumber(request.getPfNumber());
        user.setEsiNumber(request.getEsiNumber());
        user.setUanNumber(request.getUanNumber());
        user.setTaxRegime(request.getTaxRegime());

        user.setEducation(request.getEducation());
        user.setExperienceYears(request.getExperienceYears());
        user.setSkills(request.getSkills());
        user.setLanguagesKnown(request.getLanguagesKnown());
        user.setEmergencyContactName(request.getEmergencyContactName());
        user.setEmergencyContactRelationship(request.getEmergencyContactRelationship());
        user.setEmergencyContactPhone(request.getEmergencyContactPhone());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        UserEntity savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }

    @Override
    public void deleteCompanyUser(Long id) {
        UserEntity admin = getCurrentAdmin();
        CompanyEntity company = getAdminCompany(admin);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getCompany().getId().equals(company.getId())) {
            throw new UnauthorizedException("User does not belong to your company");
        }

        userRepository.delete(user);
    }

    @Override
    public UserResponse getCompanyUser(Long id) {
        UserEntity admin = getCurrentAdmin();
        CompanyEntity company = getAdminCompany(admin);

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getCompany().getId().equals(company.getId())) {
            throw new UnauthorizedException("User does not belong to your company");
        }

        UserResponse response = new UserResponse(user);
        response.setAssets(assetRepository.findByAssignedTo_IdOrderByCreatedAtDesc(user.getId())
                .stream().map(AssignedAssetResponse::new).toList());
        return response;
    }

    @Override
    public UserResponse getUserProfile(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserResponse response = new UserResponse(user);
        response.setAssets(assetRepository.findByAssignedTo_IdOrderByCreatedAtDesc(user.getId())
                .stream().map(AssignedAssetResponse::new).toList());
        return response;
    }

    @Override
    public UserResponse updateProfilePhoto(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResourceNotFoundException("Profile image is required");
        }
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Long companyId = user.getCompany() != null ? user.getCompany().getId() : 0L;
        String storedPath = fileStorageService.storeProfilePhoto(file, companyId);
        user.setProfilePhoto("/api/profile-photo/" + storedPath);
        userRepository.save(user);

        String employeeId = (user.getEmployeeId() != null && !user.getEmployeeId().isBlank())
                ? user.getEmployeeId() : String.valueOf(user.getId());
        String name = user.getFirstName()
                + (user.getLastName() != null && !user.getLastName().isBlank() ? " " + user.getLastName() : "");
        try {
            faceRecognitionService.register(employeeId, name, file);
            log.info("Face re-registered after profile photo update for employeeId={}", employeeId);
        } catch (Exception ex) {
            log.warn("Face re-register failed for employeeId={}: {}", employeeId, ex.getMessage());
        }

        return getUserProfile(user.getId());
    }

    @Override
    public List<UserResponse> getCompanyUsers() {
        UserEntity admin = getCurrentAdmin();
        CompanyEntity company = getAdminCompany(admin);

        List<UserEntity> users = userRepository.findByCompany_Id(company.getId());
        return users.stream().map(UserResponse::new).toList();
    }
}
