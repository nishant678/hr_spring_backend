package com.hr.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class BulkOperationRequest {

    @NotEmpty(message = "Company IDs cannot be empty")
    @Size(min = 1, max = 100, message = "Must select between 1 and 100 companies")
    private List<Long> companyIds;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    private Boolean sendNotification;

    @Size(max = 1000, message = "Custom message must not exceed 1000 characters")
    private String customMessage;
}
