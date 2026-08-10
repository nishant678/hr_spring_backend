package com.hr.demo.service;
import com.hr.demo.dto.LoginRequest;
import com.hr.demo.dto.RefreshTokenRequest;
import com.hr.demo.reaponse.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
}
