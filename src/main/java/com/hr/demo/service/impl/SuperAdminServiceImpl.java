package com.hr.demo.service.impl;

import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.domain.user.Role;
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
@Service
@RequiredArgsConstructor
@Transactional
public class SuperAdminServiceImpl implements SuperAdminService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CompanyResponse createCompany(CreateCompanyRequest request) {

        // 🔴 Check duplicate company email
        if (companyRepository.existsByEmail(request.getAdminEmail()))
            throw new RuntimeException("Company already registered with this email");

        // 🔴 Check duplicate admin
        if (userRepository.existsByEmail(request.getAdminEmail()))
            throw new RuntimeException("User already exists with this email");

        if (request.getEmployeeLimit() == null || request.getEmployeeLimit() <= 0)
            throw new RuntimeException("Employee limit must be greater than 0");

        // 1️⃣ Create Company
        CompanyEntity company = CompanyEntity.builder()
                .companyName(request.getCompanyName())
                .ownerName(request.getOwnerName())
                .email(request.getAdminEmail())
                .employeeLimit(request.getEmployeeLimit())
                .status(CompanyStatus.ACTIVE)
                .subscriptionStart(LocalDate.now())
                .subscriptionEnd(LocalDate.now().plusMonths(1))
                .build();

        companyRepository.save(company);

        // 2️⃣ Create Company Admin
        UserEntity admin = new UserEntity();
        admin.setEmail(request.getAdminEmail());
        admin.setPassword(passwordEncoder.encode(request.getAdminPassword()));
        admin.setRole(Role.COMPANY_ADMIN);
        admin.setCompany(company);

        userRepository.save(admin);

        return mapToResponse(company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deactivateCompany(Long companyId) {

        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + companyId));

        company.setStatus(CompanyStatus.INACTIVE);

        // 🔴 Disable all users login
        company.getUsers().forEach(user -> user.setRole(Role.DISABLED));
    }

    private CompanyResponse mapToResponse(CompanyEntity company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .email(company.getEmail())
                .status(company.getStatus())
                .build();
    }
}