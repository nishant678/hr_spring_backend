package com.hr.demo.service.impl;

import com.hr.demo.domain.user.Role;
import com.hr.demo.dto.CreateUserRequest;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.reaponse.UserResponse;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.service.UserService;
import com.hr.demo.utils.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        Role role = Role.from(request.getRole());

        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(PasswordEncoderUtil.encode(request.getPassword()))
                .role(role)
                .build();

        UserEntity savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getId()
        );
    }
}
