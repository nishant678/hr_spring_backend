package com.hr.demo.hr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoleRequest {
    private String name;
    private String description;
    private Long companyId;
}