package com.hr.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateCompanyUserRequest {

    @NotBlank
    private String firstName;
    private String middleName;
    @NotBlank
    private String lastName;
    private String employeeId;
    private LocalDate dateOfBirth;
    private String gender;
    private String maritalStatus;
    private String nationality;
    private String bloodGroup;
    private String panNumber;
    private String aadhaarNumber;
    private String passportNumber;
    private String profilePhoto;

    @NotBlank
    @Email
    private String email;

    private String password;

    private String phone;

    private Long departmentId;
    private Long designationId;
    private Long roleId;

    /** One of: HR, EMPLOYEE */
    @NotBlank
    private String role;

    // Job Information
    private String employmentType;
    private LocalDate dateOfJoining;
    private String reportingManager;
    private String workLocation;
    private String probationPeriod;
    private LocalDate confirmationDate;
    private String shift;
    private String employeeCategory;
    private String costCenter;
    private String businessUnit;

    // Contact Information
    private String officialEmail;
    private String officialPhone;
    private String personalEmail;
    private String personalPhone;
    private String currentAddress;
    private String permanentAddress;

    // Salary & Payroll
    private String paySchedule;
    private String currency;
    private BigDecimal basicSalary;
    private BigDecimal grossSalary;
    private String paymentFrequency;
    private String bankName;
    private String bankAccountNumber;
    private String ifscCode;
    private String pfNumber;
    private String esiNumber;
    private String uanNumber;
    private String taxRegime;

    // Additional Information
    private String education;
    private Integer experienceYears;
    private String skills;
    private String languagesKnown;
    private String emergencyContactName;
    private String emergencyContactRelationship;
    private String emergencyContactPhone;
}
