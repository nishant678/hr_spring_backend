package com.hr.demo.dto;

import com.hr.demo.domain.user.Role;
import lombok.Getter;

@Getter
public class AuthResponse {

    private String token;
    private String email;
    private String role;
    private Long id;

    public AuthResponse(String token, String email, Role role, Long id) {
        this.token = token;
        this.email = email;
        this.role = role.name(); // enum → string
        this.id = id;
    }
}