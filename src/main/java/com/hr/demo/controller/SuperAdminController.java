package com.hr.demo.controller;

import com.hr.demo.dto.*;
import com.hr.demo.entity.Company;
import com.hr.demo.enums.CompanyStatus;
import com.hr.demo.enums.SubscriptionPlan;
import com.hr.demo.service.SuperAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    // Company Management
    @PostMapping("/companies")
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        return ResponseEntity.ok(superAdminService.createCompany(request));
    }

    @GetMapping("/companies")
    public ResponseEntity<Page<CompanyResponse>> getAllCompanies(
            @RequestParam(required = false) CompanyStatus status,
            @RequestParam(required = false) SubscriptionPlan plan,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(superAdminService.getAllCompanies(status, plan, keyword, pageable));
    }

    @GetMapping("/companies/{companyId}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long companyId) {
        return ResponseEntity.ok(superAdminService.getCompanyById(companyId));
    }

    @PutMapping("/companies/{companyId}")
    public ResponseEntity<CompanyResponse> updateCompany(
            @PathVariable Long companyId,
            @Valid @RequestBody UpdateCompanyRequest request) {
        return ResponseEntity.ok(superAdminService.updateCompany(companyId, request));
    }

    @PostMapping("/companies/{companyId}/activate")
    public ResponseEntity<Void> activateCompany(@PathVariable Long companyId) {
        superAdminService.activateCompany(companyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/companies/{companyId}/suspend")
    public ResponseEntity<Void> suspendCompany(@PathVariable Long companyId) {
        superAdminService.suspendCompany(companyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/companies/{companyId}/terminate")
    public ResponseEntity<Void> terminateCompany(@PathVariable Long companyId) {
        superAdminService.terminateCompany(companyId);
        return ResponseEntity.ok().build();
    }

    // Subscription Management
    @PostMapping("/companies/{companyId}/subscription")
    public ResponseEntity<SubscriptionResponse> updateSubscription(
            @PathVariable Long companyId,
            @Valid @RequestBody UpdateSubscriptionRequest request) {
        return ResponseEntity.ok(superAdminService.updateSubscription(companyId, request));
    }

    @GetMapping("/companies/{companyId}/subscription")
    public ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable Long companyId) {
        return ResponseEntity.ok(superAdminService.getSubscription(companyId));
    }

    @PostMapping("/companies/{companyId}/extend-trial")
    public ResponseEntity<Void> extendTrialPeriod(
            @PathVariable Long companyId,
            @Valid @RequestBody ExtendTrialRequest request) {
        superAdminService.extendTrialPeriod(companyId, request);
        return ResponseEntity.ok().build();
    }

    // Revenue Dashboard
    @GetMapping("/dashboard/revenue")
    public ResponseEntity<RevenueDashboardResponse> getRevenueDashboard() {
        return ResponseEntity.ok(superAdminService.getRevenueDashboard());
    }

    @GetMapping("/dashboard/statistics")
    public ResponseEntity<SuperAdminDashboardResponse> getDashboardStatistics() {
        return ResponseEntity.ok(superAdminService.getDashboardStatistics());
    }

    // Company Usage Analytics
    @GetMapping("/companies/{companyId}/usage")
    public ResponseEntity<CompanyUsageResponse> getCompanyUsage(@PathVariable Long companyId) {
        return ResponseEntity.ok(superAdminService.getCompanyUsage(companyId));
    }

    @GetMapping("/companies/expiring-soon")
    public ResponseEntity<List<CompanyResponse>> getCompaniesWithExpiringSubscription(
            @RequestParam(defaultValue = "30") Integer days) {
        return ResponseEntity.ok(superAdminService.getCompaniesWithExpiringSubscription(days));
    }

    @GetMapping("/companies/at-limit")
    public ResponseEntity<List<CompanyResponse>> getCompaniesAtEmployeeLimit() {
        return ResponseEntity.ok(superAdminService.getCompaniesAtEmployeeLimit());
    }

    // System Health
    @GetMapping("/system/health")
    public ResponseEntity<SystemHealthResponse> getSystemHealth() {
        return ResponseEntity.ok(superAdminService.getSystemHealth());
    }

    @GetMapping("/system/metrics")
    public ResponseEntity<SystemMetricsResponse> getSystemMetrics() {
        return ResponseEntity.ok(superAdminService.getSystemMetrics());
    }

    // Bulk Operations
    @PostMapping("/bulk-activate")
    public ResponseEntity<BulkOperationResponse> bulkActivateCompanies(
            @Valid @RequestBody BulkOperationRequest request) {
        return ResponseEntity.ok(superAdminService.bulkActivateCompanies(request));
    }

    @PostMapping("/bulk-suspend")
    public ResponseEntity<BulkOperationResponse> bulkSuspendCompanies(
            @Valid @RequestBody BulkOperationRequest request) {
        return ResponseEntity.ok(superAdminService.bulkSuspendCompanies(request));
    }

    // Reports
    @GetMapping("/reports/companies")
    public ResponseEntity<byte[]> generateCompanyReport(
            @RequestParam(required = false) CompanyStatus status,
            @RequestParam(required = false) SubscriptionPlan plan,
            @RequestParam(defaultValue = "EXCEL") String format) {
        byte[] report = superAdminService.generateCompanyReport(status, plan, format);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=companies_report." + format.toLowerCase())
                .body(report);
    }

    @GetMapping("/reports/revenue")
    public ResponseEntity<byte[]> generateRevenueReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "EXCEL") String format) {
        byte[] report = superAdminService.generateRevenueReport(startDate, endDate, format);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=revenue_report." + format.toLowerCase())
                .body(report);
    }
}
