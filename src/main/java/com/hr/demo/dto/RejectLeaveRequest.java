package com.hr.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectLeaveRequest {

    @NotBlank
    private String rejectionReason;
}
