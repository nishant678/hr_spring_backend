package com.hr.demo.hr.dto;

import com.hr.demo.domain.user.SubscriptionPlan;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpgradeCompanyPlanRequest(
    @NotNull(message = "Subscription plan is required")
    SubscriptionPlan subscriptionPlan,

    @Min(value = 1, message = "Employee limit must be at least 1")
    Integer employeeLimit,

    String planCycle,
    LocalDate subscriptionStart,
    LocalDate subscriptionEnd
) {}
