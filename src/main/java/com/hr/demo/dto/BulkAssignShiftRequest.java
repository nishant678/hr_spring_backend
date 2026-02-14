package com.hr.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class BulkAssignShiftRequest {

    @NotEmpty(message = "Employee IDs cannot be empty")
    @Size(min = 1, max = 100, message = "Must select between 1 and 100 employees")
    private List<Long> employeeIds;

    @NotNull(message = "Shift ID is required")
    private Long shiftId;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    private Boolean sendNotification;

    @Size(max = 1000, message = "Custom message must not exceed 1000 characters")
    private String customMessage;
}
