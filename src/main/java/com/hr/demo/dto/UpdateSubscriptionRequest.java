package com.hr.demo.dto;

import com.hr.demo.enums.SubscriptionPlan;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateSubscriptionRequest {

    @NotNull(message = "Subscription plan is required")
    private SubscriptionPlan newPlan;

    @NotNull(message = "Subscription start date is required")
    private LocalDateTime subscriptionStart;

    @NotNull(message = "Subscription end date is required")
    private LocalDateTime subscriptionEnd;

    @Min(value = 1, message = "Max employees must be at least 1")
    @Max(value = 10000, message = "Max employees must not exceed 10000")
    private Integer maxEmployees;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;
}
