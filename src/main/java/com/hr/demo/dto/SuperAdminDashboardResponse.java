package com.hr.demo.dto;

import com.hr.demo.enums.CompanyStatus;
import com.hr.demo.enums.SubscriptionPlan;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SuperAdminDashboardResponse {

    // Company Statistics
    private Long totalCompanies;
    private Long activeCompanies;
    private Long suspendedCompanies;
    private Long pendingCompanies;
    private Long terminatedCompanies;
    
    // Employee Statistics
    private Long totalEmployees;
    private Long activeEmployees;
    private Long employeesOnLeave;
    private Long terminatedEmployees;
    
    // Subscription Statistics
    private Map<SubscriptionPlan, Long> companiesByPlan;
    private Long companiesExpiringSoon;
    private Long companiesAtEmployeeLimit;
    private Double averageUtilizationRate;
    
    // Recent Activities
    private List<RecentActivity> recentActivities;
    private List<CompanyAlert> companyAlerts;
    
    // System Health
    private String systemStatus;
    private Double cpuUsage;
    private Double memoryUsage;
    private Double diskUsage;
    private Long totalApiCalls;
    private Long activeApiCalls;
    
    // Growth Metrics
    private Long newCompaniesThisMonth;
    private Long newEmployeesThisMonth;
    private Double monthlyGrowthRate;
    private Double yearlyGrowthRate;

    @Data
    public static class RecentActivity {
        private String activityType;
        private String description;
        private String companyName;
        private Long companyId;
        private String timestamp;
    }

    @Data
    public static class CompanyAlert {
        private Long companyId;
        private String companyName;
        private String alertType;
        private String message;
        private String severity;
        private String timestamp;
    }
}
