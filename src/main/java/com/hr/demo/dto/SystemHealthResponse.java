package com.hr.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class SystemHealthResponse {

    private String overallStatus; // HEALTHY, WARNING, CRITICAL
    private LocalDateTime lastCheck;
    private Long uptime;
    private Double uptimePercentage;
    
    // Database Health
    private DatabaseHealth database;
    
    // Server Resources
    private ServerResources serverResources;
    
    // Application Metrics
    private ApplicationMetrics application;
    
    // External Services
    private List<ServiceHealth> externalServices;
    
    // Alerts
    private List<HealthAlert> alerts;

    @Data
    public static class DatabaseHealth {
        private String status;
        private Long connectionPoolActive;
        private Long connectionPoolIdle;
        private Long connectionPoolMax;
        private Double connectionUtilization;
        private Long totalQueries;
        private Long slowQueries;
        private Double averageQueryTime;
        private LocalDateTime lastBackup;
    }

    @Data
    public static class ServerResources {
        private Double cpuUsage;
        private Double memoryUsage;
        private Double diskUsage;
        private Long totalMemory;
        private Long availableMemory;
        private Long totalDisk;
        private Long availableDisk;
        private Double loadAverage;
    }

    @Data
    public static class ApplicationMetrics {
        private Long activeThreads;
        private Long totalRequests;
        private Long requestsPerSecond;
        private Double averageResponseTime;
        private Long errorCount;
        private Double errorRate;
        private Long activeSessions;
    }

    @Data
    public static class ServiceHealth {
        private String serviceName;
        private String status;
        private Long responseTime;
        private String lastError;
        private LocalDateTime lastCheck;
    }

    @Data
    public static class HealthAlert {
        private String type;
        private String severity;
        private String message;
        private String component;
        private LocalDateTime timestamp;
    }
}
