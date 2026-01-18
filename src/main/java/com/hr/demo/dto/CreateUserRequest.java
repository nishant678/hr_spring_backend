package com.hr.demo.dto;
import lombok.Getter;

@Getter
public class CreateUserRequest {
    private String email;
    private String password;
    private String role;
}
