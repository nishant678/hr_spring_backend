package com.hr.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExtendTrialRequest {

    @NotNull(message = "Extension days is required")
    @Min(value = 1, message = "Extension days must be at least 1")
    @Max(value = 365, message = "Extension days must not exceed 365")
    private Integer extensionDays;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    private LocalDateTime newExpiryDate;
}
