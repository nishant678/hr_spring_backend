package com.hr.demo.service.impl;

import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.domain.user.Role;
import com.hr.demo.dto.CreateUserRequest;
import com.hr.demo.entity.CompanyEntity;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.reaponse.UserResponse;
import com.hr.demo.repository.CompanyRepository;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.service.UserService;
import com.hr.demo.utils.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        // 1️⃣ Only ACTIVE company allowed
        CompanyEntity company = companyRepository
                .findByIdAndStatus(request.getCompanyId(), CompanyStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Company not found or inactive"));

        // 2️⃣ Convert role
        Role role = Role.from(request.getRole());

        // 3️⃣ Create user
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(PasswordEncoderUtil.encode(request.getPassword()))
                .role(role)
                .company(company) // ⭐ relation set
                .build();

        UserEntity savedUser = userRepository.save(user);

        // 4️⃣ Return DTO
        return new UserResponse(
                savedUser
        );
    }
}
