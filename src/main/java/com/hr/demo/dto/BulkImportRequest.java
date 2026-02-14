package com.hr.demo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class BulkImportRequest {

    @NotBlank(message = "File content is required")
    private String fileContent;

    @NotBlank(message = "File format is required")
    private String fileFormat;

    @Size(max = 500, message = "Remarks must not exceed 500 characters")
    private String remarks;

    private Boolean sendWelcomeEmail;

    private Boolean assignDefaultShift;

    private Long defaultShiftId;

    private Boolean sendNotifications;
}
