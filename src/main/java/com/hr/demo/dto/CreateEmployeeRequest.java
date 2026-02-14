package com.hr.demo.dto;

import com.hr.demo.enums.EmployeeRole;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateEmployeeRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phone;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender;

    private Long departmentId;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotBlank(message = "Employment type is required")
    private String employmentType;

    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;

    @NotNull(message = "Salary is required")
    @Min(value = 0, message = "Salary must be non-negative")
    private Double salary;

    @NotNull(message = "Role is required")
    private EmployeeRole role;

    private Long shiftId;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Postal code is required")
    private String postalCode;
}
