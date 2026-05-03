package com.hr.demo.service.impl;

import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.domain.user.Role;
import com.hr.demo.dto.AddCompanyUserRequest;
import com.hr.demo.entity.CompanyEntity;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.exceptions.UserAlreadyExistsException;
import com.hr.demo.reaponse.UserResponse;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Set<Role> ROLES_COMPANY_ADMIN_CAN_CREATE =
            EnumSet.of(Role.HR, Role.EMPLOYEE);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse addCompanyUser(AddCompanyUserRequest request) {

        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UnauthorizedException("Authenticated user not found"));

        if (admin.getRole() != Role.COMPANY_ADMIN) {
            throw new UnauthorizedException("Only company admin can add users");
        }

        CompanyEntity company = admin.getCompany();
        if (company == null) {
            throw new UnauthorizedException("Company admin is not linked to a company");
        }

        if (company.getStatus() != CompanyStatus.ACTIVE) {
            throw new UnauthorizedException("Company is not active");
        }

        Integer limit = company.getEmployeeLimit();
        if (limit != null && limit > 0) {
            long current = userRepository.countByCompany_Id(company.getId());
            if (current >= limit) {
                throw new UnauthorizedException("Employee limit reached for this company");
            }
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with this email");
        }

        Role role = Role.from(request.getRole());
        if (!ROLES_COMPANY_ADMIN_CAN_CREATE.contains(role)) {
            throw new UnauthorizedException("Company admin can only create HR or EMPLOYEE users");
        }

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .company(company)
                .build();

        UserEntity savedUser = userRepository.save(user);
        return new UserResponse(savedUser);
    }
}
