package com.hr.demo.hr.service.impl;

import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.domain.user.Role;
import com.hr.demo.domain.user.SubscriptionPlan;
import com.hr.demo.reaponse.CompanyResponse;
import com.hr.demo.hr.dto.CreateCompanyRequest;
import com.hr.demo.hr.entity.CompanyEntity;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.hr.entity.DepartmentEntity;
import com.hr.demo.hr.entity.DesignationEntity;
import com.hr.demo.hr.repository.CompanyRepository;
import com.hr.demo.hr.repository.DepartmentRepository;
import com.hr.demo.hr.repository.DesignationRepository;
import com.hr.demo.hr.repository.UserRepository;
import com.hr.demo.hr.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class SuperAdminServiceImpl implements SuperAdminService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "superAdminCompanies", allEntries = true)
    public CompanyResponse createCompany(CreateCompanyRequest request) {

        if (companyRepository.existsByEmailIgnoreCase(request.getEmail()))
            throw new RuntimeException("Company email already registered");

        if (companyRepository.existsByPhone(request.getPhone()))
            throw new RuntimeException("Company phone number already registered");

        if (userRepository.existsByEmail(request.getAdminEmail()))
            throw new RuntimeException("Admin already exists with this email");

        if (request.getEmployeeLimit() == null || request.getEmployeeLimit() <= 0)
            throw new RuntimeException("Employee limit must be greater than 0");

        LocalDate startDate = request.getSubscriptionStart() != null ? request.getSubscriptionStart() : LocalDate.now();
        LocalDate endDate = request.getSubscriptionEnd() != null ? request.getSubscriptionEnd()
                : calculatePlanExpiry(request.getSubscriptionPlan(), startDate);

        CompanyEntity company = CompanyEntity.builder()
                .companyName(request.getCompanyName())
                .ownerName(request.getOwnerName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .website(request.getWebsite())
                .logoUrl(request.getLogoUrl())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .gstNumber(request.getGstNumber())
                .panNumber(request.getPanNumber())
                .industryType(request.getIndustryType())
                .subscriptionPlan(request.getSubscriptionPlan())
                .employeeLimit(request.getEmployeeLimit())
                .subscriptionStart(startDate)
                .subscriptionEnd(endDate)
                .status(CompanyStatus.ACTIVE)
                .timezone(request.getTimezone())
                .currency(request.getCurrency())
                .attendanceMandatory(request.getAttendanceMandatory())
                .faceVerificationAttendance(Boolean.TRUE.equals(request.getFaceVerificationAttendance()))
                .autoEmailReports(request.getAutoEmailReports())
                .build();

        companyRepository.save(company);

        Map<String, Object> adminDeptAndDesig = seedDefaultDepartmentsAndDesignations(company);
        DepartmentEntity adminDept = (DepartmentEntity) adminDeptAndDesig.get("department");
        DesignationEntity adminDesig = (DesignationEntity) adminDeptAndDesig.get("designation");

        UserEntity admin = UserEntity.builder()
                .email(request.getAdminEmail())
                .password(passwordEncoder.encode(request.getAdminPassword()))
                .firstName(request.getAdminFirstName())
                .lastName(request.getAdminLastName())
                .phone(request.getAdminPhone())
                .role(Role.COMPANY_ADMIN)
                .company(company)
                .department(adminDept)
                .designation(adminDesig)
                .build();

        userRepository.save(admin);

        return mapToResponse(company);
    }

    private Map<String, Object> seedDefaultDepartmentsAndDesignations(CompanyEntity company) {
        Map<String, List<String>> deptDesignations = new java.util.LinkedHashMap<>();
        deptDesignations.put("Administration", List.of("Admin"));
        deptDesignations.put("Engineering", List.of("Software Engineer", "Senior Software Engineer", "Tech Lead", "Engineering Manager"));
        deptDesignations.put("Human Resources", List.of("HR Executive", "HR Manager"));
        deptDesignations.put("Finance", List.of("Finance Executive", "Finance Manager"));
        deptDesignations.put("Marketing", List.of("Marketing Executive", "Marketing Manager"));
        deptDesignations.put("Operations", List.of("Operations Executive", "Operations Manager"));

        DepartmentEntity adminDept = null;
        DesignationEntity adminDesig = null;

        for (Map.Entry<String, List<String>> entry : deptDesignations.entrySet()) {
            DepartmentEntity dept = DepartmentEntity.builder()
                    .name(entry.getKey())
                    .description(entry.getKey() + " department")
                    .active(true)
                    .company(company)
                    .build();
            dept = departmentRepository.save(dept);

            if ("Administration".equalsIgnoreCase(entry.getKey())) {
                adminDept = dept;
            }

            for (String desigName : entry.getValue()) {
                DesignationEntity desig = DesignationEntity.builder()
                        .name(desigName)
                        .description(desigName + " position")
                        .active(true)
                        .company(company)
                        .department(dept)
                        .build();
                desig = designationRepository.save(desig);

                if ("Administration".equalsIgnoreCase(entry.getKey()) && "Admin".equalsIgnoreCase(desigName)) {
                    adminDesig = desig;
                }
            }
        }

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("department", adminDept);
        result.put("designation", adminDesig);
        return result;
    }

    private LocalDate calculatePlanExpiry(SubscriptionPlan plan, LocalDate start) {
        return switch (plan) {
            case TRIAL -> start.plusDays(7);
            case BASIC -> start.plusMonths(1);
            case PREMIUM -> start.plusMonths(6);
            case ENTERPRISE -> start.plusYears(1);
        };
    }

    private CompanyResponse mapToResponse(CompanyEntity company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .ownerName(company.getOwnerName())
                .email(company.getEmail())
                .phone(company.getPhone())
                .website(company.getWebsite())
                .logoUrl(company.getLogoUrl())

                .address(company.getAddress())
                .city(company.getCity())
                .state(company.getState())
                .country(company.getCountry())
                .postalCode(company.getPostalCode())

                .gstNumber(company.getGstNumber())
                .panNumber(company.getPanNumber())

                .subscriptionPlan(company.getSubscriptionPlan())
                .employeeLimit(company.getEmployeeLimit())
                .subscriptionStart(company.getSubscriptionStart())
                .subscriptionEnd(company.getSubscriptionEnd())

                .timezone(company.getTimezone())
                .currency(company.getCurrency())
                .attendanceMandatory(company.getAttendanceMandatory())
                .faceVerificationAttendance(company.isFaceVerificationAttendance())
                .taskEnabled(company.isTaskEnabled())
                .autoEmailReports(company.getAutoEmailReports())
                .status(company.getStatus())
                .industryType(company.getIndustryType())
                .build();
    }

    @Override
    @org.springframework.cache.annotation.CacheEvict(value = "superAdminCompanies", allEntries = true)
    public CompanyResponse updateCompany(Long companyId, CreateCompanyRequest request) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (!company.getEmail().equalsIgnoreCase(request.getEmail())
                && companyRepository.existsByEmailIgnoreCase(request.getEmail()))
            throw new RuntimeException("Company email already registered");

        company.setCompanyName(request.getCompanyName());
        company.setOwnerName(request.getOwnerName());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());
        company.setWebsite(request.getWebsite());
        company.setLogoUrl(request.getLogoUrl());
        company.setAddress(request.getAddress());
        company.setCity(request.getCity());
        company.setState(request.getState());
        company.setCountry(request.getCountry());
        company.setPostalCode(request.getPostalCode());
        company.setGstNumber(request.getGstNumber());
        company.setPanNumber(request.getPanNumber());
        company.setSubscriptionPlan(request.getSubscriptionPlan());
        company.setEmployeeLimit(request.getEmployeeLimit());
        company.setSubscriptionStart(request.getSubscriptionStart());
        company.setSubscriptionEnd(request.getSubscriptionEnd());
        company.setTimezone(request.getTimezone());
        company.setCurrency(request.getCurrency());
        company.setAttendanceMandatory(request.getAttendanceMandatory());
        if (request.getFaceVerificationAttendance() != null) {
            company.setFaceVerificationAttendance(request.getFaceVerificationAttendance());
        }
        company.setAutoEmailReports(request.getAutoEmailReports());
        company.setIndustryType(request.getIndustryType());

        companyRepository.save(company);

        return mapToResponse(company);
    }

    @Override
    @org.springframework.cache.annotation.Cacheable(value = "superAdminCompanies", sync = true)
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAllByOrderByIdDesc().stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = "superAdminCompanies", allEntries = true)
    public CompanyResponse updatePermissions(Long companyId, boolean faceVerificationAttendance, Boolean task) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new com.hr.demo.exceptions.ResourceNotFoundException("Company not found"));
        company.setFaceVerificationAttendance(faceVerificationAttendance);
        if (task != null) company.setTaskEnabled(task);
        companyRepository.save(company);
        return mapToResponse(company);
    }

    @Override
    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = "superAdminCompanies", allEntries = true)
    public CompanyResponse deactivateCompany(Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (company.getStatus() == CompanyStatus.INACTIVE) {
            return mapToResponse(company);
        }

        company.setStatus(CompanyStatus.INACTIVE);
        return mapToResponse(companyRepository.save(company));
    }

    @Override
    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = "superAdminCompanies", allEntries = true)
    public CompanyResponse activateCompany(Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (company.getStatus() == CompanyStatus.ACTIVE) {
            return mapToResponse(company);
        }

        company.setStatus(CompanyStatus.ACTIVE);
        return mapToResponse(companyRepository.save(company));
    }

    private static final java.util.Map<SubscriptionPlan, Integer> DEFAULT_PLAN_LIMITS =
            new java.util.EnumMap<>(SubscriptionPlan.class);

    static {
        DEFAULT_PLAN_LIMITS.put(SubscriptionPlan.TRIAL, 15);
        DEFAULT_PLAN_LIMITS.put(SubscriptionPlan.BASIC, 50);
        DEFAULT_PLAN_LIMITS.put(SubscriptionPlan.PREMIUM, 200);
        DEFAULT_PLAN_LIMITS.put(SubscriptionPlan.ENTERPRISE, 1000);
    }

    @Override
    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = "superAdminCompanies", allEntries = true)
    public CompanyResponse upgradeCompanyPlan(
            Long companyId,
            com.hr.demo.hr.dto.UpgradeCompanyPlanRequest request) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new com.hr.demo.exceptions.ResourceNotFoundException("Company not found"));

        LocalDate start = request.subscriptionStart() != null
                ? request.subscriptionStart()
                : LocalDate.now();
        LocalDate end = request.subscriptionEnd() != null
                ? request.subscriptionEnd()
                : ("Yearly".equalsIgnoreCase(request.planCycle())
                        ? start.plusYears(1)
                        : calculatePlanExpiry(request.subscriptionPlan(), start));

        int limit = request.employeeLimit() != null
                ? request.employeeLimit()
                : DEFAULT_PLAN_LIMITS.getOrDefault(request.subscriptionPlan(), 50);

        company.setSubscriptionPlan(request.subscriptionPlan());
        company.setEmployeeLimit(limit);
        company.setSubscriptionStart(start);
        company.setSubscriptionEnd(end);
        company.setStatus(CompanyStatus.ACTIVE);

        return mapToResponse(companyRepository.save(company));
    }
}
