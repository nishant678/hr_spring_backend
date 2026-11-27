package com.hr.demo.website.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactInquiryRequest(
    @NotBlank(message = "First name is required") @Size(max = 80) String firstName,
    @NotBlank(message = "Last name is required") @Size(max = 80) String lastName,
    @NotBlank(message = "Email is required") @Email(message = "Invalid email format") @Size(max = 160) String email,
    @Size(max = 160) String company,
    @Size(max = 40) String phone,
    @NotBlank(message = "Service interest is required") @Size(max = 120) String serviceInterest,
    @Size(max = 80) String selectedPlan,
    @NotBlank(message = "Message is required") @Size(max = 4000) String message,
    @Size(max = 80) String sourcePage
) {}
