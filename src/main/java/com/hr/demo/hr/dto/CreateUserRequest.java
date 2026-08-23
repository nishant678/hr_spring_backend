package com.hr.demo.hr.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String email;
    private String password;
    private String role; // API input string only
    private Long companyId;
}