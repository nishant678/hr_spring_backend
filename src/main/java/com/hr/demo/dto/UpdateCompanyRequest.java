package com.hr.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateCompanyRequest {

    @Size(max = 200, message = "Company name must not exceed 200 characters")
    private String name;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phone;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    private String state;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Pattern(regexp = "^https?://.*", message = "Invalid website URL format")
    private String website;

    private String logoUrl;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9]{1}$", message = "Invalid GST number format")
    private String gstNumber;

    @Pattern(regexp = "^[A-Z]{5}[0-9]{4}[A-Z]$", message = "Invalid PAN number format")
    private String panNumber;

    @Min(value = 1, message = "Max employees must be at least 1")
    @Max(value = 10000, message = "Max employees must not exceed 10000")
    private Integer maxEmployees;
}
