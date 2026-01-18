package com.hr.demo.service.impl;

import com.hr.demo.dto.LoginRequest;
import com.hr.demo.dto.LoginResponse;
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
    public LoginResponse login(LoginRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!PasswordEncoderUtil.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(token);
    }
}
