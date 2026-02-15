package com.hr.demo.service;
import com.hr.demo.dto.AuthResponse;
import com.hr.demo.dto.LoginRequest;
import com.hr.demo.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
}
