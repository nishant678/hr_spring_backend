package com.hr.demo.dto;

import com.hr.demo.enums.LeaveType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LeavePolicyResponse {

    private Long id;
    private Long companyId;
    private String policyCode;
    private String name;
    private String description;
    private LeaveType leaveType;
    private Integer totalDaysPerYear;
    private Integer maxConsecutiveDays;
    private Integer minServiceDaysRequired;
    private Boolean carryForwardAllowed;
    private Integer maxCarryForwardDays;
    private Integer carryForwardValidityMonths;
    private Boolean encashmentAllowed;
    private Double encashmentPercentage;
    private Integer minDaysForEncashment;
    private Boolean approvalRequired;
    private Integer medicalCertificateRequiredDays;
    private Integer advanceNoticeDays;
    private Boolean restrictApplyDuringProbation;
    private Integer probationMaxDays;
    private List<String> applicableRoles;
    private List<Long> applicableDepartmentIds;
    private Boolean isActive;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
