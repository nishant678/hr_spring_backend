package com.hr.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class SystemMetricsResponse {

    private LocalDateTime timestamp;
    
    // Performance Metrics
    private PerformanceMetrics performance;
    
    // Usage Metrics
    private UsageMetrics usage;
    
    // Business Metrics
    private BusinessMetrics business;
    
    // Error Metrics
    private ErrorMetrics errors;
    
    // Security Metrics
    private SecurityMetrics security;

    @Data
    public static class PerformanceMetrics {
        private Double averageResponseTime;
        private Double p95ResponseTime;
        private Double p99ResponseTime;
        private Long requestsPerSecond;
        private Long concurrentUsers;
        private Double throughput;
        private Double cpuUtilization;
        private Double memoryUtilization;
        private Double diskIOWait;
    }

    @Data
    public static class UsageMetrics {
        private Long totalApiCalls;
        private Long apiCallsToday;
        private Long apiCallsThisWeek;
        private Long apiCallsThisMonth;
        private Map<String, Long> apiCallsByEndpoint;
        private Long totalDataTransferred;
        private Long dataTransferredToday;
        private Map<String, Long> dataTransferredByCompany;
    }

    @Data
    public static class BusinessMetrics {
        private Long activeCompanies;
        private Long activeUsers;
        private Long totalEmployees;
        private Long attendanceRecordsToday;
        private Long leaveRequestsToday;
        private Long payrollProcessedThisMonth;
        private Double revenueToday;
        private Double revenueThisMonth;
    }

    @Data
    public static class ErrorMetrics {
        private Long totalErrors;
        private Long errorsToday;
        private Long errorsThisWeek;
        private Double errorRate;
        private Map<String, Long> errorsByType;
        private Map<String, Long> errorsByEndpoint;
        private List<RecentError> recentErrors;
    }

    @Data
    public static class SecurityMetrics {
        private Long failedLoginAttempts;
        private Long successfulLogins;
        private Long suspiciousActivities;
        private Long blockedIps;
        private Map<String, Long> securityEventsByType;
        private List<SecurityEvent> recentSecurityEvents;
    }

    @Data
    public static class RecentError {
        private String errorType;
        private String message;
        private String endpoint;
        private Long companyId;
        private LocalDateTime timestamp;
    }

    @Data
    public static class SecurityEvent {
        private String eventType;
        private String description;
        private String ipAddress;
        private String userAgent;
        private Long userId;
        private LocalDateTime timestamp;
    }
}
