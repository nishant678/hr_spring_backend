package com.hr.demo.dto;

import com.hr.demo.enums.EmployeeRole;
import com.hr.demo.enums.EmployeeStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmployeeResponse {

    private Long id;
    private Long companyId;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private Long departmentId;
    private String departmentName;
    private String designation;
    private String employmentType;
    private LocalDate joiningDate;
    private Double salary;
    private EmployeeRole role;
    private EmployeeStatus status;
    private Boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
