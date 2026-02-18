package com.hr.demo.dto;

import com.hr.demo.domain.user.SubscriptionPlan;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateCompanyRequest {

    // ===== COMPANY INFO =====
    @NotBlank
    private String companyName;

    @NotBlank
    private String ownerName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    private String website;
    private String logoUrl;

    // ===== ADDRESS =====
    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String country;

    @NotBlank
    private String postalCode;

    // ===== LEGAL =====
    private String gstNumber;
    private String panNumber;

    // ===== SUBSCRIPTION =====
    @NotNull
    private SubscriptionPlan subscriptionPlan;

    @NotNull
    private Integer employeeLimit;

    private LocalDate subscriptionStart; // optional, if not provided default to today
    private LocalDate subscriptionEnd;   // optional

    private String timezone;             // optional
    private String currency;             // optional
    private Boolean attendanceMandatory; // optional
    private Boolean autoEmailReports;    // optional

    // ===== ADMIN USER =====
    @NotBlank
    private String adminFirstName;

    @NotBlank
    private String adminLastName;

    @Email
    @NotBlank
    private String adminEmail;

    @NotBlank
    private String adminPhone;

    @NotBlank
    private String adminPassword;
}