package com.hr.demo.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CompanyDashboardResponse {

    // Employee Statistics
    private Long totalEmployees;
    private Long activeEmployees;
    private Long employeesOnLeave;
    private Long newEmployeesThisMonth;
    
    // Department Statistics
    private Long totalDepartments;
    private Map<String, Long> employeesByDepartment;
    
    // Attendance Statistics
    private Double averageAttendanceRate;
    private Long presentToday;
    private Long absentToday;
    private Long lateToday;
    private Long onLeaveToday;
    
    // Leave Statistics
    private Long pendingLeaveRequests;
    private Long approvedLeavesThisMonth;
    private Map<String, Long> leavesByType;
    
    // Payroll Statistics
    private Double totalPayrollThisMonth;
    private Double averageSalary;
    private Long payrollProcessedThisMonth;
    
    // Recent Activities
    private List<RecentActivity> recentActivities;
    
    // Alerts
    private List<Alert> alerts;

    @Data
    public static class RecentActivity {
        private String type;
        private String description;
        private String employeeName;
        private String timestamp;
    }

    @Data
    public static class Alert {
        private String type;
        private String message;
        private String severity;
        private String timestamp;
    }
}
