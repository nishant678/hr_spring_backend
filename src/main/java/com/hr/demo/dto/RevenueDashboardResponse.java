package com.hr.demo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class RevenueDashboardResponse {

    private Double totalRevenue;
    private Double currentMonthRevenue;
    private Double previousMonthRevenue;
    private Double revenueGrowthPercentage;
    private Integer totalActiveCompanies;
    private Integer totalActiveEmployees;
    private Double averageRevenuePerCompany;
    private Double averageRevenuePerEmployee;
    
    // Monthly revenue data for charts
    private List<MonthlyRevenueData> monthlyRevenue;
    
    // Revenue by subscription plan
    private Map<String, Double> revenueByPlan;
    
    // Top performing companies
    private List<TopCompanyRevenue> topCompanies;
    
    // Revenue trends
    private RevenueTrends trends;

    @Data
    public static class MonthlyRevenueData {
        private String month;
        private Double revenue;
        private Integer companies;
        private Integer employees;
    }

    @Data
    public static class TopCompanyRevenue {
        private Long companyId;
        private String companyName;
        private Double revenue;
        private Integer employees;
        private String plan;
    }

    @Data
    public static class RevenueTrends {
        private Double monthlyGrowthRate;
        private Double quarterlyGrowthRate;
        private Double yearlyGrowthRate;
        private String trendDirection; // UP, DOWN, STABLE
    }
}
