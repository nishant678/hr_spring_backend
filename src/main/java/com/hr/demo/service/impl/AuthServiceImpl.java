package com.hr.demo.service.impl;

import com.hr.demo.dto.*;
import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.exceptions.InvalidEmailException;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.exceptions.WrongPasswordException;
import com.hr.demo.reaponse.AuthResponse;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.service.AuthService;
import com.hr.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {

        // 🔍 Check email exists
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidEmailException("Email not registered")
                );

        // 🔐 Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }

        if (user.getCompany() != null && user.getCompany().getStatus() != CompanyStatus.ACTIVE) {
            throw new UnauthorizedException("Company is inactive");
        }

        // 🎟 Generate token
        String token = jwtService.generateToken(user.getEmail());

        Long companyId = user.getCompany() != null ? user.getCompany().getId() : null;
        return new AuthResponse(token, user.getEmail(), user.getRole(), user.getId(), companyId);
    }

//    @Override
//    public AuthResponse register(RegisterRequest request) {
//
//        if (userRepository.findByEmail(request.getEmail()).isPresent())
//            throw new RuntimeException("Email already registered");
//
//        Role role = Role.from(request.getRole());
//
//        UserEntity user = new UserEntity();
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(role);
//
//        var savedUser = userRepository.save(user);
//
//        String token = jwtService.generateToken(savedUser.getEmail());
//
//        return new AuthResponse(token, savedUser.getEmail(), savedUser.getRole(), savedUser.getId());
//    }

}