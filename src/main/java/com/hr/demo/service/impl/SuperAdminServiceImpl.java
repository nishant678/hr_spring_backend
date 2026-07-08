package com.hr.demo.service.impl;

import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.domain.user.Role;
import com.hr.demo.domain.user.SubscriptionPlan;
import com.hr.demo.reaponse.CompanyResponse;
import com.hr.demo.dto.CreateCompanyRequest;
import com.hr.demo.entity.CompanyEntity;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.entity.DepartmentEntity;
import com.hr.demo.entity.DesignationEntity;
import com.hr.demo.repository.CompanyRepository;
import com.hr.demo.repository.DepartmentRepository;
import com.hr.demo.repository.DesignationRepository;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.service.SuperAdminService;
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
                .autoEmailReports(request.getAutoEmailReports())
                .build();

        companyRepository.save(company);

        UserEntity admin = UserEntity.builder()
                .email(request.getAdminEmail())
                .password(passwordEncoder.encode(request.getAdminPassword()))
                .role(Role.COMPANY_ADMIN)
                .company(company)
                .build();

        userRepository.save(admin);

        seedDefaultDepartmentsAndDesignations(company);

        return mapToResponse(company);
    }

    private void seedDefaultDepartmentsAndDesignations(CompanyEntity company) {
        Map<String, List<String>> deptDesignations = Map.of(
                "Engineering", List.of("Software Engineer", "Senior Software Engineer", "Tech Lead", "Engineering Manager"),
                "Human Resources", List.of("HR Executive", "HR Manager"),
                "Finance", List.of("Finance Executive", "Finance Manager"),
                "Marketing", List.of("Marketing Executive", "Marketing Manager"),
                "Operations", List.of("Operations Executive", "Operations Manager")
        );

        for (Map.Entry<String, List<String>> entry : deptDesignations.entrySet()) {
            DepartmentEntity dept = DepartmentEntity.builder()
                    .name(entry.getKey())
                    .description(entry.getKey() + " department")
                    .active(true)
                    .company(company)
                    .build();
            departmentRepository.save(dept);

            for (String desigName : entry.getValue()) {
                DesignationEntity desig = DesignationEntity.builder()
                        .name(desigName)
                        .description(desigName + " position")
                        .active(true)
                        .company(company)
                        .department(dept)
                        .build();
                designationRepository.save(desig);
            }
        }
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
                .autoEmailReports(company.getAutoEmailReports())
                .status(company.getStatus())
                .industryType(company.getIndustryType())
                .build();
    }

    @Override
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
        company.setAutoEmailReports(request.getAutoEmailReports());
        company.setIndustryType(request.getIndustryType());

        companyRepository.save(company);

        return mapToResponse(company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public CompanyResponse deactivateCompany(Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (company.getStatus() == CompanyStatus.INACTIVE) {
            return mapToResponse(company);
        }

        company.setStatus(CompanyStatus.INACTIVE);
        return mapToResponse(company);
    }

    @Override
    public CompanyResponse activateCompany(Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (company.getStatus() == CompanyStatus.ACTIVE) {
            return mapToResponse(company);
        }

        company.setStatus(CompanyStatus.ACTIVE);
        return mapToResponse(company);
    }
}