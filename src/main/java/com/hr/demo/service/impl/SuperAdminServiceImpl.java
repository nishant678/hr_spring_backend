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

    // ================= CREATE COMPANY =================
    @Override
    public CompanyResponse createCompany(CreateCompanyRequest request) {

        // ---------- VALIDATION ----------
        if (companyRepository.existsByEmail(request.getCompanyEmail()))
            throw new RuntimeException("Company email already registered");

        if (userRepository.existsByEmail(request.getAdminEmail()))
            throw new RuntimeException("Admin already exists with this email");

        if (request.getEmployeeLimit() == null || request.getEmployeeLimit() <= 0)
            throw new RuntimeException("Employee limit must be greater than 0");

        // ---------- PLAN DURATION ----------
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = calculatePlanExpiry(request.getSubscriptionPlan(), startDate);

        // ---------- CREATE COMPANY ----------
        CompanyEntity company = CompanyEntity.builder()
                .companyName(request.getCompanyName())
                .ownerName(request.getOwnerName())
                .email(request.getCompanyEmail())
                .phone(request.getCompanyPhone())
                .website(request.getWebsite())
                .logoUrl(request.getLogoUrl())

                // Address
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())

                // Legal
                .gstNumber(request.getGstNumber())
                .panNumber(request.getPanNumber())

                // Subscription
                .subscriptionPlan(request.getSubscriptionPlan())
//                .employeeLimit(request.getEmployeeLimit())
                .subscriptionStart(startDate)
                .subscriptionEnd(endDate)
                .status(CompanyStatus.ACTIVE)
                .build();

        companyRepository.save(company);

        // ---------- CREATE COMPANY ADMIN ----------
        UserEntity admin = new UserEntity();
        admin.setEmail(request.getAdminEmail());
        admin.setPassword(passwordEncoder.encode(request.getAdminPassword()));
        admin.setRole(Role.COMPANY_ADMIN);
        admin.setCompany(company);

        userRepository.save(admin);

        return mapToResponse(company);
    }

    // ================= GET ALL =================
    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= DEACTIVATE =================
    @Override
    public void deactivateCompany(Long companyId) {

        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        company.setStatus(CompanyStatus.INACTIVE);

        // disable all users login
        if (company.getUsers() != null) {
            company.getUsers().forEach(user -> user.setRole(Role.DISABLED));
        }
    }

    // ================= PLAN EXPIRY LOGIC =================
    private LocalDate calculatePlanExpiry(SubscriptionPlan plan, LocalDate start) {

        return switch (plan) {
            case TRIAL -> start.plusDays(7);
            case BASIC -> start.plusMonths(1);
            case PREMIUM -> start.plusMonths(6);
            case ENTERPRISE -> start.plusYears(1);
        };
    }

    // ================= RESPONSE =================
    private CompanyResponse mapToResponse(CompanyEntity company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .email(company.getEmail())
                .status(company.getStatus())
                .build();
    }
}