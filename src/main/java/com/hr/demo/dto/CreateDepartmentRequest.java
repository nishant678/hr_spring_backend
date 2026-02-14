package com.hr.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateDepartmentRequest {

    @NotBlank(message = "Department name is required")
    @Size(max = 200, message = "Department name must not exceed 200 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private Long headOfDepartmentId;

    private Long parentDepartmentId;

    @Min(value = 0, message = "Budget limit must be non-negative")
    private Double budgetLimit;
}
