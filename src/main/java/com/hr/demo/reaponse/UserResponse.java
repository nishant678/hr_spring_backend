package com.hr.demo.reaponse;

import com.hr.demo.domain.user.Role;
import com.hr.demo.entity.UserEntity;
import lombok.Getter;
@Getter
public class UserResponse {
    private String email;
    private String role;
    private Long id;
    private Long companyId;
    private String password;

    public UserResponse(UserEntity user) {
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.id = user.getId();
        this.companyId = user.getCompany().getId();
        this.password = user.getPassword();
    }
}
