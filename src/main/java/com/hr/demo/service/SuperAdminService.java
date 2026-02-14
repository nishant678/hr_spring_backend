package com.hr.demo.service;

import com.hr.demo.dto.*;
import com.hr.demo.enums.CompanyStatus;
import com.hr.demo.enums.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SuperAdminService {

    // Company Management
    CompanyResponse createCompany(CreateCompanyRequest request);
    Page<CompanyResponse> getAllCompanies(CompanyStatus status, SubscriptionPlan plan, String keyword, Pageable pageable);
    CompanyResponse getCompanyById(Long companyId);
    CompanyResponse updateCompany(Long companyId, UpdateCompanyRequest request);
    void activateCompany(Long companyId);
    void suspendCompany(Long companyId);
    void terminateCompany(Long companyId);

    // Subscription Management
    SubscriptionResponse updateSubscription(Long companyId, UpdateSubscriptionRequest request);
    SubscriptionResponse getSubscription(Long companyId);
    void extendTrialPeriod(Long companyId, ExtendTrialRequest request);

    // Dashboard
    RevenueDashboardResponse getRevenueDashboard();
    SuperAdminDashboardResponse getDashboardStatistics();
    CompanyUsageResponse getCompanyUsage(Long companyId);

    // Company Analytics
    List<CompanyResponse> getCompaniesWithExpiringSubscription(Integer days);
    List<CompanyResponse> getCompaniesAtEmployeeLimit();

    // System Health
    SystemHealthResponse getSystemHealth();
    SystemMetricsResponse getSystemMetrics();

    // Bulk Operations
    BulkOperationResponse bulkActivateCompanies(BulkOperationRequest request);
    BulkOperationResponse bulkSuspendCompanies(BulkOperationRequest request);

    // Reports
    byte[] generateCompanyReport(CompanyStatus status, SubscriptionPlan plan, String format);
    byte[] generateRevenueReport(String startDate, String endDate, String format);
}
