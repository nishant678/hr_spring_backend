package com.hr.demo.reaponse;

import com.hr.demo.domain.user.Role;
import lombok.Getter;
@Getter
public class UserResponse {
    private String email;
    private String role;
    private Long id;

    public UserResponse(String email, Role role, Long id) {
        this.email = email;
        this.role = role.name();
        this.id = id;
    }
}
