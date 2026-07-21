package com.hr.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectExpenseRequest {

    @NotBlank
    private String rejectionReason;
}
