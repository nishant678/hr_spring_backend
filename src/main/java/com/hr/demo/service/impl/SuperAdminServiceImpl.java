package com.hr.demo.service.impl;

import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.domain.user.Role;
import com.hr.demo.domain.user.SubscriptionPlan;
import com.hr.demo.dto.CompanyResponse;
import com.hr.demo.dto.CreateCompanyRequest;
import com.hr.demo.entity.CompanyEntity;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.repository.CompanyRepository;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.service.SuperAdminService;
//import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.hr.demo.domain.user.SubscriptionPlan.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SuperAdminServiceImpl implements SuperAdminService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CompanyResponse createCompany(CreateCompanyRequest request) {

        if (companyRepository.existsByEmailIgnoreCase(request.getEmail()))
            throw new RuntimeException("Company email already registered");

        if (userRepository.existsByEmail(request.getAdminEmail()))
            throw new RuntimeException("Admin already exists with this email");

        if (request.getEmployeeLimit() == null || request.getEmployeeLimit() <= 0)
            throw new RuntimeException("Employee limit must be greater than 0");

        LocalDate startDate = request.getSubscriptionStart() != null ? request.getSubscriptionStart() : LocalDate.now();
        LocalDate endDate = request.getSubscriptionEnd() != null ?
                request.getSubscriptionEnd() :
                calculatePlanExpiry(request.getSubscriptionPlan(), startDate);

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

        // Create Admin User
        UserEntity admin = UserEntity.builder()
                .email(request.getAdminEmail())
                .password(passwordEncoder.encode(request.getAdminPassword()))
                .role(Role.COMPANY_ADMIN)
                .company(company)
                .build();

        userRepository.save(admin);

        return mapToResponse(company);
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
                .email(company.getEmail())
                .status(company.getStatus())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public void deactivateCompany(Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setStatus(CompanyStatus.INACTIVE);
        if (company.getUsers() != null) {
            company.getUsers().forEach(user -> user.setRole(Role.DISABLED));
        }
    }
}