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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CompanyResponse createCompany(CreateCompanyRequest request) {

        CompanyEntity company = CompanyEntity.builder()
                .companyName(request.getCompanyName())
                .ownerName(request.getOwnerName())
                .email(request.getAdminEmail())
                .employeeLimit(request.getEmployeeLimit())
                .status(CompanyStatus.ACTIVE)
                .subscriptionStart(LocalDate.now())
                .subscriptionEnd(LocalDate.now().plusMonths(1))
                .build();

        company = companyRepository.save(company);

        // Create company admin user
        UserEntity admin = new UserEntity();
        admin.setEmail(request.getAdminEmail());
        admin.setPassword(passwordEncoder.encode(request.getAdminPassword()));
        admin.setRole(Role.ADMIN.name());
        admin.setCompany(company);

        userRepository.save(admin);

        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .email(company.getEmail())
                .status(company.getStatus())
                .build();
    }

    @Override
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(c -> CompanyResponse.builder()
                        .id(c.getId())
                        .companyName(c.getCompanyName())
                        .email(c.getEmail())
                        .status(c.getStatus())
                        .build())
                .toList();
    }

    @Override
    public void deactivateCompany(Long companyId) {
        CompanyEntity company = companyRepository.findById(companyId).orElseThrow();
        company.setStatus(CompanyStatus.INACTIVE);
        companyRepository.save(company);
    }
}