package com.hr.demo.service.impl;

import com.hr.demo.dto.*;
import com.hr.demo.entity.Company;
import com.hr.demo.entity.Employee;
import com.hr.demo.enums.CompanyStatus;
import com.hr.demo.enums.SubscriptionPlan;
import com.hr.demo.repository.CompanyRepository;
import com.hr.demo.repository.EmployeeRepository;
import com.hr.demo.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SuperAdminServiceImpl implements SuperAdminService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public CompanyResponse createCompany(CreateCompanyRequest request) {
        log.info("Creating new company: {}", request.getName());
        
        // Check if company already exists
        if (companyRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Company with email " + request.getEmail() + " already exists");
        }

        // Create company entity
        Company company = Company.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .website(request.getWebsite())
                .logoUrl(request.getLogoUrl())
                .gstNumber(request.getGstNumber())
                .panNumber(request.getPanNumber())
                .subscriptionPlan(request.getSubscriptionPlan())
                .subscriptionStart(request.getSubscriptionStart())
                .subscriptionEnd(request.getSubscriptionEnd())
                .maxEmployees(request.getMaxEmployees())
                .currentEmployees(0)
                .status(CompanyStatus.ACTIVE)
                .isActive(true)
                .build();

        company = companyRepository.save(company);

        // Create admin user for the company
        createCompanyAdmin(company, request);

        return convertToCompanyResponse(company);
    }

    @Override
    public Page<CompanyResponse> getAllCompanies(CompanyStatus status, SubscriptionPlan plan, String keyword, Pageable pageable) {
        log.info("Fetching companies with filters - status: {}, plan: {}, keyword: {}", status, plan, keyword);
        
        Page<Company> companies;
        if (keyword != null && !keyword.trim().isEmpty()) {
            companies = companyRepository.searchActiveCompanies(keyword.trim(), pageable);
        } else if (status != null) {
            companies = companyRepository.findByStatusWithPagination(status, pageable);
        } else if (plan != null) {
            companies = companyRepository.findBySubscriptionPlanWithPagination(plan, pageable);
        } else {
            companies = companyRepository.findAll(pageable);
        }

        return companies.map(this::convertToCompanyResponse);
    }

    @Override
    public CompanyResponse getCompanyById(Long companyId) {
        log.info("Fetching company details for ID: {}", companyId);
        
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        
        return convertToCompanyResponse(company);
    }

    @Override
    public CompanyResponse updateCompany(Long companyId, UpdateCompanyRequest request) {
        log.info("Updating company with ID: {}", companyId);
        
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));

        // Update fields
        if (request.getName() != null) company.setName(request.getName());
        if (request.getEmail() != null) company.setEmail(request.getEmail());
        if (request.getPhone() != null) company.setPhone(request.getPhone());
        if (request.getAddress() != null) company.setAddress(request.getAddress());
        if (request.getCity() != null) company.setCity(request.getCity());
        if (request.getState() != null) company.setState(request.getState());
        if (request.getCountry() != null) company.setCountry(request.getCountry());
        if (request.getPostalCode() != null) company.setPostalCode(request.getPostalCode());
        if (request.getWebsite() != null) company.setWebsite(request.getWebsite());
        if (request.getLogoUrl() != null) company.setLogoUrl(request.getLogoUrl());
        if (request.getGstNumber() != null) company.setGstNumber(request.getGstNumber());
        if (request.getPanNumber() != null) company.setPanNumber(request.getPanNumber());
        if (request.getMaxEmployees() != null) company.setMaxEmployees(request.getMaxEmployees());

        company = companyRepository.save(company);
        
        return convertToCompanyResponse(company);
    }

    @Override
    public void activateCompany(Long companyId) {
        log.info("Activating company with ID: {}", companyId);
        
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        
        company.setStatus(CompanyStatus.ACTIVE);
        company.setIsActive(true);
        companyRepository.save(company);
    }

    @Override
    public void suspendCompany(Long companyId) {
        log.info("Suspending company with ID: {}", companyId);
        
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        
        company.setStatus(CompanyStatus.SUSPENDED);
        company.setIsActive(false);
        companyRepository.save(company);
    }

    @Override
    public void terminateCompany(Long companyId) {
        log.info("Terminating company with ID: {}", companyId);
        
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        
        company.setStatus(CompanyStatus.TERMINATED);
        company.setIsActive(false);
        companyRepository.save(company);
    }

    @Override
    public SubscriptionResponse updateSubscription(Long companyId, UpdateSubscriptionRequest request) {
        log.info("Updating subscription for company ID: {}", companyId);
        
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));

        company.setSubscriptionPlan(request.getNewPlan());
        company.setSubscriptionStart(request.getSubscriptionStart());
        company.setSubscriptionEnd(request.getSubscriptionEnd());
        if (request.getMaxEmployees() != null) {
            company.setMaxEmployees(request.getMaxEmployees());
        }

        company = companyRepository.save(company);
        
        return convertToSubscriptionResponse(company);
    }

    @Override
    public SubscriptionResponse getSubscription(Long companyId) {
        log.info("Getting subscription details for company ID: {}", companyId);
        
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        
        return convertToSubscriptionResponse(company);
    }

    @Override
    public void extendTrialPeriod(Long companyId, ExtendTrialRequest request) {
        log.info("Extending trial period for company ID: {} by {} days", companyId, request.getExtensionDays());
        
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));

        LocalDateTime newExpiryDate = company.getSubscriptionEnd().plusDays(request.getExtensionDays());
        company.setSubscriptionEnd(newExpiryDate);
        
        companyRepository.save(company);
    }

    @Override
    public RevenueDashboardResponse getRevenueDashboard() {
        log.info("Generating revenue dashboard");
        
        RevenueDashboardResponse response = new RevenueDashboardResponse();
        
        // Calculate total revenue (simplified calculation based on subscription plans)
        List<Company> allCompanies = companyRepository.findAll();
        List<Company> activeCompanies = allCompanies.stream()
                .filter(c -> c.getStatus() == CompanyStatus.ACTIVE)
                .collect(Collectors.toList());
        
        double totalRevenue = activeCompanies.stream()
                .mapToDouble(this::calculateMonthlyRevenue)
                .sum();
        
        response.setTotalRevenue(totalRevenue);
        response.setTotalActiveCompanies(activeCompanies.size());
        response.setTotalActiveEmployees(employeeRepository.sumTotalActiveEmployees().intValue());
        
        // TODO: Implement more detailed revenue calculations
        
        return response;
    }

    @Override
    public SuperAdminDashboardResponse getDashboardStatistics() {
        log.info("Generating super admin dashboard statistics");
        
        SuperAdminDashboardResponse response = new SuperAdminDashboardResponse();
        
        // Company statistics
        response.setTotalCompanies(companyRepository.count());
        response.setActiveCompanies(companyRepository.countByStatus(CompanyStatus.ACTIVE));
        response.setSuspendedCompanies(companyRepository.countByStatus(CompanyStatus.SUSPENDED));
        response.setPendingCompanies(companyRepository.countByStatus(CompanyStatus.PENDING));
        response.setTerminatedCompanies(companyRepository.countByStatus(CompanyStatus.TERMINATED));
        
        // Employee statistics
        response.setTotalEmployees(employeeRepository.count());
        response.setActiveEmployees(employeeRepository.sumTotalActiveEmployees());
        
        return response;
    }

    @Override
    public CompanyUsageResponse getCompanyUsage(Long companyId) {
        log.info("Getting usage statistics for company ID: {}", companyId);
        
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));
        
        CompanyUsageResponse response = new CompanyUsageResponse();
        response.setCompanyId(companyId);
        response.setCompanyName(company.getName());
        response.setCompanyCode(company.getCompanyCode());
        response.setTotalEmployees(company.getCurrentEmployees());
        response.setMaxEmployees(company.getMaxEmployees());
        response.setUtilizationPercentage((double) company.getCurrentEmployees() / company.getMaxEmployees() * 100);
        response.setIsAtLimit(company.getCurrentEmployees() >= company.getMaxEmployees());
        
        // TODO: Implement more detailed usage tracking
        
        return response;
    }

    @Override
    public List<CompanyResponse> getCompaniesWithExpiringSubscription(Integer days) {
        log.info("Getting companies with subscription expiring in {} days", days);
        
        LocalDateTime threshold = LocalDateTime.now().plusDays(days);
        List<Company> companies = companyRepository.findCompaniesWithExpiredSubscription(threshold);
        
        return companies.stream()
                .map(this::convertToCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyResponse> getCompaniesAtEmployeeLimit() {
        log.info("Getting companies at employee limit");
        
        List<Company> companies = companyRepository.findCompaniesAtEmployeeLimit();
        
        return companies.stream()
                .map(this::convertToCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SystemHealthResponse getSystemHealth() {
        log.info("Getting system health status");
        
        SystemHealthResponse response = new SystemHealthResponse();
        response.setOverallStatus("HEALTHY");
        response.setLastCheck(LocalDateTime.now());
        
        // TODO: Implement actual health checks
        
        return response;
    }

    @Override
    public SystemMetricsResponse getSystemMetrics() {
        log.info("Getting system metrics");
        
        SystemMetricsResponse response = new SystemMetricsResponse();
        response.setTimestamp(LocalDateTime.now());
        
        // TODO: Implement actual metrics collection
        
        return response;
    }

    @Override
    public BulkOperationResponse bulkActivateCompanies(BulkOperationRequest request) {
        log.info("Bulk activating {} companies", request.getCompanyIds().size());
        
        BulkOperationResponse response = new BulkOperationResponse();
        response.setOperationType("ACTIVATE");
        response.setTimestamp(LocalDateTime.now());
        response.setTotalRequested(request.getCompanyIds().size());
        
        int successCount = 0;
        int failureCount = 0;
        
        for (Long companyId : request.getCompanyIds()) {
            try {
                activateCompany(companyId);
                successCount++;
            } catch (Exception e) {
                failureCount++;
                log.error("Failed to activate company {}: {}", companyId, e.getMessage());
            }
        }
        
        response.setSuccessful(successCount);
        response.setFailed(failureCount);
        response.setOverallSuccess(failureCount == 0);
        
        return response;
    }

    @Override
    public BulkOperationResponse bulkSuspendCompanies(BulkOperationRequest request) {
        log.info("Bulk suspending {} companies", request.getCompanyIds().size());
        
        BulkOperationResponse response = new BulkOperationResponse();
        response.setOperationType("SUSPEND");
        response.setTimestamp(LocalDateTime.now());
        response.setTotalRequested(request.getCompanyIds().size());
        
        int successCount = 0;
        int failureCount = 0;
        
        for (Long companyId : request.getCompanyIds()) {
            try {
                suspendCompany(companyId);
                successCount++;
            } catch (Exception e) {
                failureCount++;
                log.error("Failed to suspend company {}: {}", companyId, e.getMessage());
            }
        }
        
        response.setSuccessful(successCount);
        response.setFailed(failureCount);
        response.setOverallSuccess(failureCount == 0);
        
        return response;
    }

    @Override
    public byte[] generateCompanyReport(CompanyStatus status, SubscriptionPlan plan, String format) {
        log.info("Generating company report in {} format", format);
        
        // TODO: Implement report generation (Excel/PDF)
        throw new UnsupportedOperationException("Report generation not yet implemented");
    }

    @Override
    public byte[] generateRevenueReport(String startDate, String endDate, String format) {
        log.info("Generating revenue report from {} to {} in {}", startDate, endDate, format);
        
        // TODO: Implement revenue report generation
        throw new UnsupportedOperationException("Revenue report generation not yet implemented");
    }

    // Helper methods
    private void createCompanyAdmin(Company company, CreateCompanyRequest request) {
        // TODO: Implement admin user creation
        log.info("Creating admin user for company: {}", company.getName());
    }

    private CompanyResponse convertToCompanyResponse(Company company) {
        CompanyResponse response = new CompanyResponse();
        response.setId(company.getId());
        response.setName(company.getName());
        response.setEmail(company.getEmail());
        response.setPhone(company.getPhone());
        response.setAddress(company.getAddress());
        response.setCity(company.getCity());
        response.setState(company.getState());
        response.setCountry(company.getCountry());
        response.setPostalCode(company.getPostalCode());
        response.setCompanyCode(company.getCompanyCode());
        response.setLogoUrl(company.getLogoUrl());
        response.setWebsite(company.getWebsite());
        response.setStatus(company.getStatus());
        response.setSubscriptionPlan(company.getSubscriptionPlan());
        response.setSubscriptionStart(company.getSubscriptionStart());
        response.setSubscriptionEnd(company.getSubscriptionEnd());
        response.setMaxEmployees(company.getMaxEmployees());
        response.setCurrentEmployees(company.getCurrentEmployees());
        response.setGstNumber(company.getGstNumber());
        response.setPanNumber(company.getPanNumber());
        response.setIsActive(company.getIsActive());
        response.setCreatedAt(company.getCreatedAt());
        response.setUpdatedAt(company.getUpdatedAt());
        
        // Calculate additional fields
        long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(
                LocalDateTime.now(), company.getSubscriptionEnd());
        response.setDaysUntilExpiry((int) daysUntilExpiry);
        response.setIsExpiringSoon(daysUntilExpiry <= 30);
        response.setIsExpired(daysUntilExpiry < 0);
        response.setUtilizationPercentage((double) company.getCurrentEmployees() / company.getMaxEmployees() * 100);
        response.setIsAtEmployeeLimit(company.getCurrentEmployees() >= company.getMaxEmployees());
        
        return response;
    }

    private SubscriptionResponse convertToSubscriptionResponse(Company company) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setCompanyId(company.getId());
        response.setCompanyName(company.getName());
        response.setCurrentPlan(company.getSubscriptionPlan());
        response.setSubscriptionStart(company.getSubscriptionStart());
        response.setSubscriptionEnd(company.getSubscriptionEnd());
        response.setMaxEmployees(company.getMaxEmployees());
        response.setCurrentEmployees(company.getCurrentEmployees());
        response.setIsActive(company.getIsActive());
        
        long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(
                LocalDateTime.now(), company.getSubscriptionEnd());
        response.setDaysUntilExpiry((int) daysUntilExpiry);
        response.setIsExpiringSoon(daysUntilExpiry <= 30);
        response.setIsExpired(daysUntilExpiry < 0);
        response.setUtilizationPercentage((double) company.getCurrentEmployees() / company.getMaxEmployees() * 100);
        
        return response;
    }

    private double calculateMonthlyRevenue(Company company) {
        // Simplified revenue calculation based on subscription plan
        switch (company.getSubscriptionPlan()) {
            case BASIC:
                return 1999.0;
            case STANDARD:
                return 3499.0;
            case PREMIUM:
                return 6999.0;
            case ENTERPRISE:
                return 9999.0;
            default:
                return 0.0;
        }
    }
}
