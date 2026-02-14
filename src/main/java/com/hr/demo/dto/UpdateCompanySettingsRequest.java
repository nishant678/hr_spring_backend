package com.hr.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateCompanySettingsRequest {

    // Attendance Settings
    private Boolean gpsRequired;

    private Boolean faceRecognitionRequired;

    @Min(value = 1, message = "Office radius must be at least 1 meter")
    private Integer officeRadiusMeters;

    @Min(value = -90, message = "Latitude must be between -90 and 90")
    @Max(value = 90, message = "Latitude must be between -90 and 90")
    private Double officeLatitude;

    @Min(value = -180, message = "Longitude must be between -180 and 180")
    @Max(value = 180, message = "Longitude must be between -180 and 180")
    private Double officeLongitude;

    @Min(value = 0, message = "Grace period must be non-negative")
    private Integer gracePeriodMinutes;

    // Leave Settings
    private Boolean leaveApprovalRequired;

    @Min(value = 0, message = "Max carry forward days must be non-negative")
    private Integer maxCarryForwardDays;

    private Boolean leaveEncashmentEnabled;

    // Payroll Settings
    @Pattern(regexp = "^[1-9]|1[0-9]|2[0-8]$", message = "Payroll day must be between 1 and 28")
    private String payrollProcessingDay;

    private Boolean overtimeCalculationEnabled;

    @Min(value = 0, message = "Overtime rate must be non-negative")
    private Double overtimeRate;

    // Notification Settings
    private Boolean emailNotificationsEnabled;

    private Boolean smsNotificationsEnabled;

    private Boolean pushNotificationsEnabled;

    // Security Settings
    private Boolean twoFactorAuthEnabled;

    @Min(value = 0, message = "Password expiry days must be non-negative")
    private Integer passwordExpiryDays;

    private Boolean sessionTimeoutEnabled;

    // Working Hours
    private String workingDays;

    private String workingHoursStart;

    private String workingHoursEnd;

    private String lunchBreakStart;

    private String lunchBreakEnd;
}
