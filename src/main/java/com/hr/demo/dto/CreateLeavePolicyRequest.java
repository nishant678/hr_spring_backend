package com.hr.demo.dto;

import com.hr.demo.enums.EmployeeRole;
import com.hr.demo.enums.LeaveType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateLeavePolicyRequest {

    @NotBlank(message = "Policy name is required")
    @Size(max = 200, message = "Policy name must not exceed 200 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Leave type is required")
    private LeaveType leaveType;

    @NotNull(message = "Total days per year is required")
    @Min(value = 0, message = "Total days must be non-negative")
    private Integer totalDaysPerYear;

    @Min(value = 0, message = "Max consecutive days must be non-negative")
    private Integer maxConsecutiveDays;

    @Min(value = 0, message = "Min service days must be non-negative")
    private Integer minServiceDaysRequired;

    private Boolean carryForwardAllowed;

    @Min(value = 0, message = "Max carry forward days must be non-negative")
    private Integer maxCarryForwardDays;

    @Min(value = 0, message = "Carry forward validity must be non-negative")
    private Integer carryForwardValidityMonths;

    private Boolean encashmentAllowed;

    @Min(value = 0, message = "Encashment percentage must be non-negative")
    @Max(value = 100, message = "Encashment percentage must not exceed 100")
    private Double encashmentPercentage;

    @Min(value = 0, message = "Min days for encashment must be non-negative")
    private Integer minDaysForEncashment;

    private Boolean approvalRequired;

    @Min(value = 0, message = "Medical certificate days must be non-negative")
    private Integer medicalCertificateRequiredDays;

    @Min(value = 0, message = "Advance notice days must be non-negative")
    private Integer advanceNoticeDays;

    private Boolean restrictApplyDuringProbation;

    @Min(value = 0, message = "Probation max days must be non-negative")
    private Integer probationMaxDays;

    private List<EmployeeRole> applicableRoles;

    private List<Long> applicableDepartmentIds;
}
