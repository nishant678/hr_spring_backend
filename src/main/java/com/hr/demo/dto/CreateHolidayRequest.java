package com.hr.demo.dto;

import com.hr.demo.enums.HolidayType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateHolidayRequest {

    @NotBlank(message = "Holiday name is required")
    @Size(max = 200, message = "Holiday name must not exceed 200 characters")
    private String name;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Holiday type is required")
    private HolidayType holidayType;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private Boolean isRecurring;

    private Boolean isOptional;

    private Boolean isHalfDay;

    private Boolean applicableToAll;

    private List<Long> applicableDepartmentIds;

    private List<Long> applicableEmployeeIds;
}
