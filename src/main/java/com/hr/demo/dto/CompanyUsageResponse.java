package com.hr.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class CompanyUsageResponse {

    private Long companyId;
    private String companyName;
    private String companyCode;
    
    // Employee Usage
    private Integer totalEmployees;
    private Integer activeEmployees;
    private Integer maxEmployees;
    private Double utilizationPercentage;
    private Boolean isAtLimit;
    
    // API Usage
    private Long totalApiCalls;
    private Long apiCallsThisMonth;
    private Long apiCallsToday;
    private Double averageApiCallsPerDay;
    
    // Storage Usage
    private Long totalStorageUsed;
    private Long storageLimit;
    private Double storageUtilizationPercentage;
    
    // Feature Usage
    private Map<String, Boolean> featuresUsed;
    private List<FeatureUsageDetail> featureUsageDetails;
    
    // Attendance Statistics
    private Long totalAttendanceRecords;
    private Long attendanceRecordsThisMonth;
    private Double averageDailyAttendance;
    
    // Leave Management
    private Long totalLeaveRequests;
    private Long leaveRequestsThisMonth;
    private Long approvedLeaves;
    private Long pendingLeaves;
    
    // Payroll Statistics
    private Long totalPayrollRecords;
    private Long payrollProcessedThisMonth;
    private Double totalPayrollAmount;
    
    // System Performance
    private Double averageResponseTime;
    private Long uptime;
    private Long downtime;
    private Double uptimePercentage;
    
    // Last Activity
    private LocalDateTime lastLogin;
    private LocalDateTime lastApiCall;
    private LocalDateTime lastDataUpdate;

    @Data
    public static class FeatureUsageDetail {
        private String featureName;
        private Boolean isActive;
        private Long usageCount;
        private LocalDateTime lastUsed;
    }
}
