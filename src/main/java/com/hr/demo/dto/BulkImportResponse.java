package com.hr.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BulkImportResponse {

    private String operationType;
    private LocalDateTime timestamp;
    private Integer totalRecords;
    private Integer successful;
    private Integer failed;
    private Integer skipped;
    private Boolean overallSuccess;
    private String message;
    
    private List<BulkImportResult> results;
    private List<String> errors;
    private List<String> warnings;

    @Data
    public static class BulkImportResult {
        private Integer rowNumber;
        private String employeeName;
        private String employeeEmail;
        private Boolean success;
        private String message;
        private Long employeeId;
    }
}
