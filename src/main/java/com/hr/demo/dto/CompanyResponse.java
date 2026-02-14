package com.hr.demo.dto;

import com.hr.demo.enums.CompanyStatus;
import com.hr.demo.enums.SubscriptionPlan;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String companyCode;
    private String logoUrl;
    private String website;
    private CompanyStatus status;
    private SubscriptionPlan subscriptionPlan;
    private LocalDateTime subscriptionStart;
    private LocalDateTime subscriptionEnd;
    private Integer maxEmployees;
    private Integer currentEmployees;
    private String gstNumber;
    private String panNumber;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for dashboard
    private Integer daysUntilExpiry;
    private Boolean isExpiringSoon;
    private Boolean isExpired;
    private Boolean isAtEmployeeLimit;
    private Double utilizationPercentage;
}
