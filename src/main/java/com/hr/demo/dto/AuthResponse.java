package com.hr.demo.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;
    private String email;
    private String role;
    private Long id;
    
    public AuthResponse(String token, String email, String role, Long id) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.id = id;
    }
}
