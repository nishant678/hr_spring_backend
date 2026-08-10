package com.hr.demo.service.impl;

import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.dto.*;
import com.hr.demo.entity.RefreshTokenEntity;
import com.hr.demo.exceptions.InvalidEmailException;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.exceptions.WrongPasswordException;
import com.hr.demo.reaponse.AuthResponse;
import com.hr.demo.repository.RefreshTokenRepository;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.security.JwtService;
import com.hr.demo.security.JwtService.TokenPair;
import com.hr.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidEmailException("Email not registered"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongPasswordException("Wrong password");
        }

        if (user.getCompany() != null && user.getCompany().getStatus() != CompanyStatus.ACTIVE) {
            throw new UnauthorizedException("Company is inactive");
        }

        TokenPair pair = jwtService.generateTokenPair(user.getEmail());
        saveRefreshToken(pair.refreshToken(), user.getId());

        return AuthResponse.fromPair(pair, user);
    }

    private void saveRefreshToken(String token, Long userId) {
        refreshTokenRepository.revokeAllForUser(userId);
        refreshTokenRepository.deleteExpiredOrRevoked(LocalDateTime.now());
        RefreshTokenEntity entity = RefreshTokenEntity.builder()
                .token(token)
                .userId(userId)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();
        refreshTokenRepository.save(entity);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshTokenEntity stored = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (stored.isRevoked()) {
            throw new UnauthorizedException("Refresh token has been revoked");
        }
        if (stored.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh token expired");
        }

        jwtService.isRefreshToken(request.getRefreshToken());
        String email = jwtService.extractUsername(request.getRefreshToken());
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        TokenPair pair = jwtService.generateTokenPair(email);
        saveRefreshToken(pair.refreshToken(), user.getId());

        return AuthResponse.fromPair(pair, user);
    }
}
