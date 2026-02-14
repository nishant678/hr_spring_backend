package com.hr.demo.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class ShiftResponse {

    private Long id;
    private Long companyId;
    private String shiftCode;
    private String name;
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
    private Integer breakDurationMinutes;
    private Integer gracePeriodMinutes;
    private Integer overtimeThresholdMinutes;
    private Double halfDayThresholdHours;
    private Integer lateThresholdMinutes;
    private Integer earlyDepartureThresholdMinutes;
    private Double officeLatitude;
    private Double officeLongitude;
    private Integer officeRadiusMeters;
    private Boolean isGpsRequired;
    private Boolean isFaceRecognitionRequired;
    private Boolean isWeekendWorkAllowed;
    private Boolean isHolidayWorkAllowed;
    private List<String> workingDays;
    private Integer totalEmployees;
    private String status;
    private Boolean isActive;
    private Double workingHours;
}
