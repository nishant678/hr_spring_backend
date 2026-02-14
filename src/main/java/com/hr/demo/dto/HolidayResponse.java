package com.hr.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HolidayResponse {

    private Long id;
    private Long companyId;
    private String holidayCode;
    private String name;
    private LocalDate date;
    private String dayOfWeek;
    private String holidayType;
    private String description;
    private Boolean isRecurring;
    private Boolean isOptional;
    private Boolean isHalfDay;
    private Boolean applicableToAll;
    private List<Long> applicableDepartmentIds;
    private List<Long> applicableEmployeeIds;
    private Boolean notificationSent;
    private LocalDateTime notificationSentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
