package com.hr.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanySettingsResponse {

    private Long companyId;
    private String companyName;
    private String companyCode;
    
    // Attendance Settings
    private Boolean gpsRequired;
    private Boolean faceRecognitionRequired;
    private Integer officeRadiusMeters;
    private Double officeLatitude;
    private Double officeLongitude;
    private Integer gracePeriodMinutes;
    
    // Leave Settings
    private Boolean leaveApprovalRequired;
    private Integer maxCarryForwardDays;
    private Boolean leaveEncashmentEnabled;
    
    // Payroll Settings
    private String payrollProcessingDay;
    private Boolean overtimeCalculationEnabled;
    private Double overtimeRate;
    
    // Notification Settings
    private Boolean emailNotificationsEnabled;
    private Boolean smsNotificationsEnabled;
    private Boolean pushNotificationsEnabled;
    
    // Security Settings
    private Boolean twoFactorAuthEnabled;
    private Integer passwordExpiryDays;
    private Boolean sessionTimeoutEnabled;
    
    // Working Hours
    private String workingDays;
    private String workingHoursStart;
    private String workingHoursEnd;
    private String lunchBreakStart;
    private String lunchBreakEnd;
    
    private LocalDateTime lastUpdated;
}
