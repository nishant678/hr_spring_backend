package com.hr.demo.hr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDesignationRequest {
    @NotBlank
    private String name;
    private String description;
    private Long departmentId;
}
