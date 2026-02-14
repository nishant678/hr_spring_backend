package com.hr.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BulkOperationResponse {

    private String operationType;
    private LocalDateTime timestamp;
    private Integer totalRequested;
    private Integer successful;
    private Integer failed;
    private Boolean overallSuccess;
    private String message;
    
    private List<BulkOperationResult> results;
    private List<String> errors;

    @Data
    public static class BulkOperationResult {
        private Long companyId;
        private String companyName;
        private Boolean success;
        private String message;
        private LocalDateTime processedAt;
    }
}
