package com.hr.demo.dto;

import com.hr.demo.enums.SubscriptionPlan;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateCompanyRequest {

    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name must not exceed 200 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phone;

    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Pattern(regexp = "^https?://.*", message = "Invalid website URL format")
    private String website;

    private String logoUrl;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9]{1}$", message = "Invalid GST number format")
    private String gstNumber;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "Invalid PAN number format")
    private String panNumber;

    @NotNull(message = "Subscription plan is required")
    private SubscriptionPlan subscriptionPlan;

    @NotNull(message = "Subscription start date is required")
    private LocalDateTime subscriptionStart;

    @NotNull(message = "Subscription end date is required")
    private LocalDateTime subscriptionEnd;

    @NotNull(message = "Max employees is required")
    @Min(value = 1, message = "Max employees must be at least 1")
    @Max(value = 10000, message = "Max employees must not exceed 10000")
    private Integer maxEmployees;

    // Admin user details for company
    @NotBlank(message = "Admin first name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String adminFirstName;

    @NotBlank(message = "Admin last name is required")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String adminLastName;

    @NotBlank(message = "Admin email is required")
    @Email(message = "Invalid admin email format")
    private String adminEmail;

    @NotBlank(message = "Admin phone is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid admin phone number format")
    private String adminPhone;

    @NotBlank(message = "Admin password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]", 
             message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String adminPassword;
}
