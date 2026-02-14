package com.hr.demo.service;

import com.hr.demo.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyAdminService {

    // Department Management
    DepartmentResponse createDepartment(CreateDepartmentRequest request);
    Page<DepartmentResponse> getAllDepartments(Pageable pageable);
    DepartmentResponse getDepartmentById(Long departmentId);
    DepartmentResponse updateDepartment(Long departmentId, UpdateDepartmentRequest request);
    void deleteDepartment(Long departmentId);

    // Employee Management
    EmployeeResponse createEmployee(CreateEmployeeRequest request);
    Page<EmployeeResponse> getAllEmployees(Long departmentId, String keyword, Pageable pageable);
    EmployeeResponse getEmployeeById(Long employeeId);
    EmployeeResponse updateEmployee(Long employeeId, UpdateEmployeeRequest request);
    void deleteEmployee(Long employeeId);
    void activateEmployee(Long employeeId);
    void deactivateEmployee(Long employeeId);

    // Shift Management
    ShiftResponse createShift(CreateShiftRequest request);
    Page<ShiftResponse> getAllShifts(Pageable pageable);
    ShiftResponse getShiftById(Long shiftId);
    ShiftResponse updateShift(Long shiftId, UpdateShiftRequest request);
    void deleteShift(Long shiftId);

    // Holiday Management
    HolidayResponse createHoliday(CreateHolidayRequest request);
    List<HolidayResponse> getAllHolidays(Integer year);
    HolidayResponse getHolidayById(Long holidayId);
    HolidayResponse updateHoliday(Long holidayId, UpdateHolidayRequest request);
    void deleteHoliday(Long holidayId);

    // Leave Policy Management
    LeavePolicyResponse createLeavePolicy(CreateLeavePolicyRequest request);
    List<LeavePolicyResponse> getAllLeavePolicies();
    LeavePolicyResponse getLeavePolicyById(Long policyId);
    LeavePolicyResponse updateLeavePolicy(Long policyId, UpdateLeavePolicyRequest request);
    void deleteLeavePolicy(Long policyId);

    // Dashboard
    CompanyDashboardResponse getCompanyDashboard();

    // Bulk Operations
    BulkImportResponse bulkImportEmployees(BulkImportRequest request);
    BulkOperationResponse bulkAssignDepartment(BulkAssignDepartmentRequest request);
    BulkOperationResponse bulkAssignShift(BulkAssignShiftRequest request);

    // Reports
    byte[] generateEmployeeReport(Long departmentId, String format);
    byte[] generateAttendanceSummaryReport(String startDate, String endDate, String format);

    // Settings
    CompanySettingsResponse getCompanySettings();
    CompanySettingsResponse updateCompanySettings(UpdateCompanySettingsRequest request);
}
