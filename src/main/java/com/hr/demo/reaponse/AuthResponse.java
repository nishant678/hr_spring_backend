package com.hr.demo.reaponse;

import com.hr.demo.domain.user.Role;
import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;
    private String email;
    private String role;
    private Long id;
    private Long companyId;

    public AuthResponse(String token, String email, Role role, Long id, Long companyId) {
        this.token = token;
        this.email = email;
        this.role = role.name();
        this.id = id;
        this.companyId = companyId;
    }
}
