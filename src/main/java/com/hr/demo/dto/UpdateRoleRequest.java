package com.hr.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRoleRequest {
    private String name;
    private String description;
}