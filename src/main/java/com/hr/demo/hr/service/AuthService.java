package com.hr.demo.hr.service;
import com.hr.demo.hr.dto.LoginRequest;
import com.hr.demo.hr.dto.RefreshTokenRequest;
import com.hr.demo.reaponse.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(RefreshTokenRequest request);
}
