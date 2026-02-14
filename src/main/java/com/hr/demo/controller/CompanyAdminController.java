package com.hr.demo.controller;

import com.hr.demo.dto.*;
import com.hr.demo.service.CompanyAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company-admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'COMPANY_ADMIN')")
public class CompanyAdminController {

    private final CompanyAdminService companyAdminService;

    // Department Management
    @PostMapping("/departments")
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody CreateDepartmentRequest request) {
        return ResponseEntity.ok(companyAdminService.createDepartment(request));
    }

    @GetMapping("/departments")
    public ResponseEntity<Page<DepartmentResponse>> getAllDepartments(Pageable pageable) {
        return ResponseEntity.ok(companyAdminService.getAllDepartments(pageable));
    }

    @GetMapping("/departments/{departmentId}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long departmentId) {
        return ResponseEntity.ok(companyAdminService.getDepartmentById(departmentId));
    }

    @PutMapping("/departments/{departmentId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long departmentId,
            @Valid @RequestBody UpdateDepartmentRequest request) {
        return ResponseEntity.ok(companyAdminService.updateDepartment(departmentId, request));
    }

    @DeleteMapping("/departments/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long departmentId) {
        companyAdminService.deleteDepartment(departmentId);
        return ResponseEntity.ok().build();
    }

    // Employee Management
    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.ok(companyAdminService.createEmployee(request));
    }

    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(companyAdminService.getAllEmployees(departmentId, keyword, pageable));
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long employeeId) {
        return ResponseEntity.ok(companyAdminService.getEmployeeById(employeeId));
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody UpdateEmployeeRequest request) {
        return ResponseEntity.ok(companyAdminService.updateEmployee(employeeId, request));
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
        companyAdminService.deleteEmployee(employeeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/employees/{employeeId}/activate")
    public ResponseEntity<Void> activateEmployee(@PathVariable Long employeeId) {
        companyAdminService.activateEmployee(employeeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/employees/{employeeId}/deactivate")
    public ResponseEntity<Void> deactivateEmployee(@PathVariable Long employeeId) {
        companyAdminService.deactivateEmployee(employeeId);
        return ResponseEntity.ok().build();
    }

    // Shift Management
    @PostMapping("/shifts")
    public ResponseEntity<ShiftResponse> createShift(@Valid @RequestBody CreateShiftRequest request) {
        return ResponseEntity.ok(companyAdminService.createShift(request));
    }

    @GetMapping("/shifts")
    public ResponseEntity<Page<ShiftResponse>> getAllShifts(Pageable pageable) {
        return ResponseEntity.ok(companyAdminService.getAllShifts(pageable));
    }

    @GetMapping("/shifts/{shiftId}")
    public ResponseEntity<ShiftResponse> getShiftById(@PathVariable Long shiftId) {
        return ResponseEntity.ok(companyAdminService.getShiftById(shiftId));
    }

    @PutMapping("/shifts/{shiftId}")
    public ResponseEntity<ShiftResponse> updateShift(
            @PathVariable Long shiftId,
            @Valid @RequestBody UpdateShiftRequest request) {
        return ResponseEntity.ok(companyAdminService.updateShift(shiftId, request));
    }

    @DeleteMapping("/shifts/{shiftId}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long shiftId) {
        companyAdminService.deleteShift(shiftId);
        return ResponseEntity.ok().build();
    }

    // Holiday Calendar Management
    @PostMapping("/holidays")
    public ResponseEntity<HolidayResponse> createHoliday(@Valid @RequestBody CreateHolidayRequest request) {
        return ResponseEntity.ok(companyAdminService.createHoliday(request));
    }

    @GetMapping("/holidays")
    public ResponseEntity<List<HolidayResponse>> getAllHolidays(
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(companyAdminService.getAllHolidays(year));
    }

    @GetMapping("/holidays/{holidayId}")
    public ResponseEntity<HolidayResponse> getHolidayById(@PathVariable Long holidayId) {
        return ResponseEntity.ok(companyAdminService.getHolidayById(holidayId));
    }

    @PutMapping("/holidays/{holidayId}")
    public ResponseEntity<HolidayResponse> updateHoliday(
            @PathVariable Long holidayId,
            @Valid @RequestBody UpdateHolidayRequest request) {
        return ResponseEntity.ok(companyAdminService.updateHoliday(holidayId, request));
    }

    @DeleteMapping("/holidays/{holidayId}")
    public ResponseEntity<Void> deleteHoliday(@PathVariable Long holidayId) {
        companyAdminService.deleteHoliday(holidayId);
        return ResponseEntity.ok().build();
    }

    // Leave Policy Management
    @PostMapping("/leave-policies")
    public ResponseEntity<LeavePolicyResponse> createLeavePolicy(@Valid @RequestBody CreateLeavePolicyRequest request) {
        return ResponseEntity.ok(companyAdminService.createLeavePolicy(request));
    }

    @GetMapping("/leave-policies")
    public ResponseEntity<List<LeavePolicyResponse>> getAllLeavePolicies() {
        return ResponseEntity.ok(companyAdminService.getAllLeavePolicies());
    }

    @GetMapping("/leave-policies/{policyId}")
    public ResponseEntity<LeavePolicyResponse> getLeavePolicyById(@PathVariable Long policyId) {
        return ResponseEntity.ok(companyAdminService.getLeavePolicyById(policyId));
    }

    @PutMapping("/leave-policies/{policyId}")
    public ResponseEntity<LeavePolicyResponse> updateLeavePolicy(
            @PathVariable Long policyId,
            @Valid @RequestBody UpdateLeavePolicyRequest request) {
        return ResponseEntity.ok(companyAdminService.updateLeavePolicy(policyId, request));
    }

    @DeleteMapping("/leave-policies/{policyId}")
    public ResponseEntity<Void> deleteLeavePolicy(@PathVariable Long policyId) {
        companyAdminService.deleteLeavePolicy(policyId);
        return ResponseEntity.ok().build();
    }

    // Company Dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<CompanyDashboardResponse> getCompanyDashboard() {
        return ResponseEntity.ok(companyAdminService.getCompanyDashboard());
    }

    // Employee Bulk Operations
    @PostMapping("/employees/bulk-import")
    public ResponseEntity<BulkImportResponse> bulkImportEmployees(@Valid @RequestBody BulkImportRequest request) {
        return ResponseEntity.ok(companyAdminService.bulkImportEmployees(request));
    }

    @PostMapping("/employees/bulk-assign-department")
    public ResponseEntity<BulkOperationResponse> bulkAssignDepartment(
            @Valid @RequestBody BulkAssignDepartmentRequest request) {
        return ResponseEntity.ok(companyAdminService.bulkAssignDepartment(request));
    }

    @PostMapping("/employees/bulk-assign-shift")
    public ResponseEntity<BulkOperationResponse> bulkAssignShift(
            @Valid @RequestBody BulkAssignShiftRequest request) {
        return ResponseEntity.ok(companyAdminService.bulkAssignShift(request));
    }

    // Reports
    @GetMapping("/reports/employees")
    public ResponseEntity<byte[]> generateEmployeeReport(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "EXCEL") String format) {
        byte[] report = companyAdminService.generateEmployeeReport(departmentId, format);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=employees_report." + format.toLowerCase())
                .body(report);
    }

    @GetMapping("/reports/attendance-summary")
    public ResponseEntity<byte[]> generateAttendanceSummaryReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "EXCEL") String format) {
        byte[] report = companyAdminService.generateAttendanceSummaryReport(startDate, endDate, format);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=attendance_summary." + format.toLowerCase())
                .body(report);
    }

    // Settings
    @GetMapping("/settings")
    public ResponseEntity<CompanySettingsResponse> getCompanySettings() {
        return ResponseEntity.ok(companyAdminService.getCompanySettings());
    }

    @PutMapping("/settings")
    public ResponseEntity<CompanySettingsResponse> updateCompanySettings(
            @Valid @RequestBody UpdateCompanySettingsRequest request) {
        return ResponseEntity.ok(companyAdminService.updateCompanySettings(request));
    }
}
