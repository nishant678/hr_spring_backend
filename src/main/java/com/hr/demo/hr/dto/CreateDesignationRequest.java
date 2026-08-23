package com.hr.demo.hr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDesignationRequest {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Long departmentId;
}
