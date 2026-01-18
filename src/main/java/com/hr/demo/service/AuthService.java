package com.hr.demo.service;
import com.hr.demo.dto.LoginRequest;
import com.hr.demo.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
