package com.hr.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DepartmentResponse {

    private Long id;
    private Long companyId;
    private String departmentCode;
    private String name;
    private String description;
    private Long headOfDepartmentId;
    private String headOfDepartmentName;
    private Long parentDepartmentId;
    private String parentDepartmentName;
    private Integer totalEmployees;
    private Double budgetLimit;
    private Double currentBudget;
    private String status;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<DepartmentResponse> subDepartments;
}
