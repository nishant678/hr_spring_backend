package com.hr.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class UpdateShiftRequest {

    @Size(max = 200, message = "Shift name must not exceed 200 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalTime breakStartTime;

    private LocalTime breakEndTime;

    @Min(value = 0, message = "Break duration must be non-negative")
    private Integer breakDurationMinutes;

    @Min(value = 0, message = "Grace period must be non-negative")
    private Integer gracePeriodMinutes;

    @Min(value = 0, message = "Overtime threshold must be non-negative")
    private Integer overtimeThresholdMinutes;

    @Min(value = 0, message = "Half day threshold must be non-negative")
    private Double halfDayThresholdHours;

    private Double officeLatitude;

    private Double officeLongitude;

    @Min(value = 1, message = "Office radius must be at least 1 meter")
    private Integer officeRadiusMeters;

    private Boolean isGpsRequired;

    private Boolean isFaceRecognitionRequired;

    private Boolean isWeekendWorkAllowed;

    private Boolean isHolidayWorkAllowed;

    private List<String> workingDays;
}
