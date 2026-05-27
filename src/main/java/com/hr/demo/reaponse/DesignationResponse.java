package com.hr.demo.reaponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DesignationResponse {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private Long companyId;
    private Long departmentId;
}
