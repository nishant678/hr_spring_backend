package com.hr.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCompanyRequest {

    @NotBlank
    private String companyName;

    @NotBlank
    private String ownerName;

    @Email
    private String adminEmail;

    @NotBlank
    private String adminPassword;

    private Integer employeeLimit;
}