package com.hr.demo.dto;

import com.hr.demo.enums.SubscriptionPlan;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubscriptionResponse {

    private Long companyId;
    private String companyName;
    private SubscriptionPlan currentPlan;
    private LocalDateTime subscriptionStart;
    private LocalDateTime subscriptionEnd;
    private Integer maxEmployees;
    private Integer currentEmployees;
    private Integer daysUntilExpiry;
    private Boolean isExpiringSoon;
    private Boolean isExpired;
    private Boolean isActive;
    private Double utilizationPercentage;
    private LocalDateTime lastUpdated;
    private String status;
}
