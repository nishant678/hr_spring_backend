package com.hr.demo.service.impl;

import com.hr.demo.dto.AuthResponse;
import com.hr.demo.dto.LoginRequest;
import com.hr.demo.dto.RegisterRequest;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.security.JwtService;
import com.hr.demo.service.AuthService;
import com.hr.demo.utils.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!PasswordEncoderUtil.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getRole(), user.getId());
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }

        // Create new user
        var user = new com.hr.demo.entity.UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(PasswordEncoderUtil.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "USER");

        var savedUser = userRepository.save(user);

        // Generate token for automatic login
        String token = jwtService.generateToken(savedUser.getEmail());
        
        return new AuthResponse(token, savedUser.getEmail(), savedUser.getRole(), savedUser.getId());
    }
}
