package com.hr.demo.dto;

import com.hr.demo.enums.EmployeeRole;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateEmployeeRequest {

    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phone;

    private LocalDate dateOfBirth;

    private String gender;

    private Long departmentId;

    private String designation;

    private String employmentType;

    private LocalDate joiningDate;

    @Min(value = 0, message = "Salary must be non-negative")
    private Double salary;

    private EmployeeRole role;

    private Long shiftId;

    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;
}
