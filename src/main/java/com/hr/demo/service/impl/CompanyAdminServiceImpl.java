package com.hr.demo.service.impl;

import com.hr.demo.dto.*;
import com.hr.demo.entity.*;
import com.hr.demo.enums.*;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.repository.*;
import com.hr.demo.service.CompanyAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompanyAdminServiceImpl implements CompanyAdminService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final HolidayCalendarRepository holidayCalendarRepository;
    private final LeavePolicyRepository leavePolicyRepository;
    private final CompanyRepository companyRepository;
    private final AttendanceRepository attendanceRepository;

    // Department Management
    @Override
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        Department department = new Department();
        department.setCompanyId(1L); // TODO: Get from security context
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setHeadOfDepartmentId(request.getHeadOfDepartmentId());
        department.setParentDepartmentId(request.getParentDepartmentId());
        department.setBudgetLimit(request.getBudgetLimit());
        department.setTotalEmployees(0);
        department.setCurrentBudget(0.0);
        department.setStatus(com.hr.demo.enums.DepartmentStatus.ACTIVE);
        
        Department saved = departmentRepository.save(department);
        return convertToDepartmentResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable)
                .map(this::convertToDepartmentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        return convertToDepartmentResponse(department);
    }

    @Override
    public DepartmentResponse updateDepartment(Long departmentId, UpdateDepartmentRequest request) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setHeadOfDepartmentId(request.getHeadOfDepartmentId());
        department.setParentDepartmentId(request.getParentDepartmentId());
        department.setBudgetLimit(request.getBudgetLimit());
        department.setUpdatedAt(LocalDateTime.now());
        
        Department saved = departmentRepository.save(department);
        return convertToDepartmentResponse(saved);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        department.setStatus(com.hr.demo.enums.DepartmentStatus.INACTIVE);
        department.setUpdatedAt(LocalDateTime.now());
        departmentRepository.save(department);
    }

    // Employee Management
    @Override
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        Employee employee = new Employee();
        employee.setCompanyId(1L); // TODO: Get from security context
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setPassword(request.getPassword());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setGender(request.getGender());
        employee.setDepartmentId(request.getDepartmentId());
        employee.setDesignation(request.getDesignation());
        employee.setEmploymentType(request.getEmploymentType());
        employee.setJoiningDate(request.getJoiningDate());
        employee.setSalary(request.getSalary());
        employee.setRole(request.getRole());
        employee.setShiftId(request.getShiftId());
        employee.setAddress(request.getAddress());
        employee.setCity(request.getCity());
        employee.setState(request.getState());
        employee.setCountry(request.getCountry());
        employee.setPostalCode(request.getPostalCode());
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setGpsEnabled(true);
        
        Employee saved = employeeRepository.save(employee);
        return convertToEmployeeResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployees(Long departmentId, String keyword, Pageable pageable) {
        if (departmentId != null && keyword != null) {
            List<Employee> employees = employeeRepository.findByCompanyIdAndDepartmentIdAndIsActive(1L, departmentId, true);
            return new org.springframework.data.domain.PageImpl<>(employees.stream()
                    .map(this::convertToEmployeeResponse)
                    .collect(Collectors.toList()), pageable, employees.size());
        } else if (departmentId != null) {
            List<Employee> employees = employeeRepository.findByCompanyIdAndDepartmentIdAndIsActive(1L, departmentId, true);
            return new org.springframework.data.domain.PageImpl<>(employees.stream()
                    .map(this::convertToEmployeeResponse)
                    .collect(Collectors.toList()), pageable, employees.size());
        } else {
            return employeeRepository.findByCompanyIdAndIsActive(1L, true, pageable)
                    .map(this::convertToEmployeeResponse);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return convertToEmployeeResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long employeeId, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartmentId(request.getDepartmentId());
        employee.setDesignation(request.getDesignation());
        employee.setSalary(request.getSalary());
        employee.setAddress(request.getAddress());
        employee.setCity(request.getCity());
        employee.setState(request.getState());
        employee.setCountry(request.getCountry());
        employee.setPostalCode(request.getPostalCode());
        employee.setUpdatedAt(LocalDateTime.now());
        
        Employee saved = employeeRepository.save(employee);
        return convertToEmployeeResponse(saved);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setStatus(EmployeeStatus.TERMINATED);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    @Override
    public void activateEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setStatus(EmployeeStatus.ACTIVE);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    @Override
    public void deactivateEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setStatus(EmployeeStatus.INACTIVE);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    // Shift Management
    @Override
    public ShiftResponse createShift(CreateShiftRequest request) {
        Shift shift = new Shift();
        shift.setCompanyId(1L); // TODO: Get from security context
        shift.setName(request.getName());
        shift.setDescription(request.getDescription());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());
        shift.setBreakStartTime(request.getBreakStartTime());
        shift.setBreakEndTime(request.getBreakEndTime());
        shift.setBreakDurationMinutes(request.getBreakDurationMinutes());
        shift.setGracePeriodMinutes(request.getGracePeriodMinutes() != null ? request.getGracePeriodMinutes() : 15);
        shift.setOvertimeThresholdMinutes(request.getOvertimeThresholdMinutes());
        shift.setHalfDayThresholdHours(request.getHalfDayThresholdHours());
        shift.setLateThresholdMinutes(0);
        shift.setEarlyDepartureThresholdMinutes(0);
        shift.setOfficeLatitude(request.getOfficeLatitude());
        shift.setOfficeLongitude(request.getOfficeLongitude());
        shift.setOfficeRadiusMeters(request.getOfficeRadiusMeters());
        shift.setIsGpsRequired(request.getIsGpsRequired() != null ? request.getIsGpsRequired() : false);
        shift.setIsFaceRecognitionRequired(request.getIsFaceRecognitionRequired() != null ? request.getIsFaceRecognitionRequired() : false);
        shift.setIsWeekendWorkAllowed(request.getIsWeekendWorkAllowed() != null ? request.getIsWeekendWorkAllowed() : false);
        shift.setIsHolidayWorkAllowed(request.getIsHolidayWorkAllowed() != null ? request.getIsHolidayWorkAllowed() : false);
        if (request.getWorkingDays() != null) {
            List<java.time.DayOfWeek> workingDays = new ArrayList<>();
            for (String day : request.getWorkingDays()) {
                workingDays.add(java.time.DayOfWeek.valueOf(day));
            }
            shift.setWorkingDays(workingDays);
        } else {
            shift.setWorkingDays(java.util.Arrays.asList(
                    java.time.DayOfWeek.MONDAY, 
                    java.time.DayOfWeek.TUESDAY, 
                    java.time.DayOfWeek.WEDNESDAY, 
                    java.time.DayOfWeek.THURSDAY, 
                    java.time.DayOfWeek.FRIDAY));
        }
        shift.setTotalEmployees(0);
        shift.setStatus(com.hr.demo.enums.ShiftStatus.ACTIVE);
        
        Shift saved = shiftRepository.save(shift);
        return convertToShiftResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShiftResponse> getAllShifts(Pageable pageable) {
        return shiftRepository.findAll(pageable)
                .map(this::convertToShiftResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftResponse getShiftById(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found"));
        return convertToShiftResponse(shift);
    }

    @Override
    public ShiftResponse updateShift(Long shiftId, UpdateShiftRequest request) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found"));
        
        shift.setName(request.getName());
        shift.setDescription(request.getDescription());
        shift.setStartTime(request.getStartTime());
        shift.setEndTime(request.getEndTime());
        shift.setBreakStartTime(request.getBreakStartTime());
        shift.setBreakEndTime(request.getBreakEndTime());
        shift.setBreakDurationMinutes(request.getBreakDurationMinutes());
        shift.setGracePeriodMinutes(request.getGracePeriodMinutes());
        shift.setOvertimeThresholdMinutes(request.getOvertimeThresholdMinutes());
        shift.setHalfDayThresholdHours(request.getHalfDayThresholdHours());
        shift.setOfficeLatitude(request.getOfficeLatitude());
        shift.setOfficeLongitude(request.getOfficeLongitude());
        shift.setOfficeRadiusMeters(request.getOfficeRadiusMeters());
        shift.setIsGpsRequired(request.getIsGpsRequired());
        shift.setIsFaceRecognitionRequired(request.getIsFaceRecognitionRequired());
        shift.setIsWeekendWorkAllowed(request.getIsWeekendWorkAllowed());
        shift.setIsHolidayWorkAllowed(request.getIsHolidayWorkAllowed());
        shift.setWorkingDays(request.getWorkingDays().stream()
                    .map(day -> java.time.DayOfWeek.valueOf(day))
                    .collect(Collectors.toList()));
        shift.setUpdatedAt(LocalDateTime.now());
        
        Shift saved = shiftRepository.save(shift);
        return convertToShiftResponse(saved);
    }

    @Override
    public void deleteShift(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found"));
        shift.setStatus(com.hr.demo.enums.ShiftStatus.INACTIVE);
        shift.setUpdatedAt(LocalDateTime.now());
        shiftRepository.save(shift);
    }

    // Holiday Management
    @Override
    public HolidayResponse createHoliday(CreateHolidayRequest request) {
        HolidayCalendar holiday = new HolidayCalendar();
        holiday.setCompanyId(1L); // TODO: Get from security context
        holiday.setName(request.getName());
        holiday.setDate(request.getDate());
        holiday.setHolidayType(request.getHolidayType());
        holiday.setDescription(request.getDescription());
        holiday.setDayOfWeek(request.getDate().getDayOfWeek().name());
        holiday.setIsRecurring(request.getIsRecurring() != null ? request.getIsRecurring() : false);
        holiday.setIsOptional(request.getIsOptional() != null ? request.getIsOptional() : false);
        holiday.setIsHalfDay(request.getIsHalfDay() != null ? request.getIsHalfDay() : false);
        holiday.setApplicableToAll(request.getApplicableToAll() != null ? request.getApplicableToAll() : true);
        holiday.setApplicableDepartmentIds(request.getApplicableDepartmentIds());
        holiday.setApplicableEmployeeIds(request.getApplicableEmployeeIds());
        
        HolidayCalendar saved = holidayCalendarRepository.save(holiday);
        return convertToHolidayResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolidayResponse> getAllHolidays(Integer year) {
        if (year != null) {
            return holidayCalendarRepository.findByYear(year).stream()
                    .map(this::convertToHolidayResponse)
                    .collect(Collectors.toList());
        }
        return holidayCalendarRepository.findAll().stream()
                .map(this::convertToHolidayResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HolidayResponse getHolidayById(Long holidayId) {
        HolidayCalendar holiday = holidayCalendarRepository.findById(holidayId)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));
        return convertToHolidayResponse(holiday);
    }

    @Override
    public HolidayResponse updateHoliday(Long holidayId, UpdateHolidayRequest request) {
        HolidayCalendar holiday = holidayCalendarRepository.findById(holidayId)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));
        
        holiday.setName(request.getName());
        holiday.setDate(request.getDate());
        holiday.setHolidayType(request.getHolidayType());
        holiday.setDescription(request.getDescription());
        holiday.setIsRecurring(request.getIsRecurring());
        holiday.setIsOptional(request.getIsOptional());
        holiday.setIsHalfDay(request.getIsHalfDay());
        holiday.setApplicableToAll(request.getApplicableToAll());
        holiday.setApplicableDepartmentIds(request.getApplicableDepartmentIds());
        holiday.setApplicableEmployeeIds(request.getApplicableEmployeeIds());
        holiday.setUpdatedAt(LocalDateTime.now());
        
        HolidayCalendar saved = holidayCalendarRepository.save(holiday);
        return convertToHolidayResponse(saved);
    }

    @Override
    public void deleteHoliday(Long holidayId) {
        HolidayCalendar holiday = holidayCalendarRepository.findById(holidayId)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));
        holidayCalendarRepository.delete(holiday);
    }

    // Leave Policy Management
    @Override
    public LeavePolicyResponse createLeavePolicy(CreateLeavePolicyRequest request) {
        LeavePolicy policy = new LeavePolicy();
        policy.setCompanyId(1L); // TODO: Get from security context
        policy.setName(request.getName());
        policy.setDescription(request.getDescription());
        policy.setLeaveType(request.getLeaveType());
        policy.setTotalDaysPerYear(request.getTotalDaysPerYear());
        policy.setMaxConsecutiveDays(request.getMaxConsecutiveDays());
        policy.setMinServiceDaysRequired(request.getMinServiceDaysRequired());
        policy.setCarryForwardAllowed(request.getCarryForwardAllowed() != null ? request.getCarryForwardAllowed() : false);
        policy.setMaxCarryForwardDays(request.getMaxCarryForwardDays());
        policy.setCarryForwardValidityMonths(request.getCarryForwardValidityMonths());
        policy.setEncashmentAllowed(request.getEncashmentAllowed() != null ? request.getEncashmentAllowed() : false);
        policy.setEncashmentPercentage(request.getEncashmentPercentage());
        policy.setMinDaysForEncashment(request.getMinDaysForEncashment());
        policy.setApprovalRequired(request.getApprovalRequired() != null ? request.getApprovalRequired() : true);
        policy.setMedicalCertificateRequiredDays(request.getMedicalCertificateRequiredDays());
        policy.setAdvanceNoticeDays(request.getAdvanceNoticeDays());
        policy.setRestrictApplyDuringProbation(request.getRestrictApplyDuringProbation() != null ? request.getRestrictApplyDuringProbation() : false);
        policy.setProbationMaxDays(request.getProbationMaxDays());
        policy.setApplicableRoles(request.getApplicableRoles());
        policy.setApplicableDepartmentIds(request.getApplicableDepartmentIds());
        policy.setIsActive(true);
        
        LeavePolicy saved = leavePolicyRepository.save(policy);
        return convertToLeavePolicyResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeavePolicyResponse> getAllLeavePolicies() {
        return leavePolicyRepository.findAll().stream()
                .map(this::convertToLeavePolicyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public LeavePolicyResponse getLeavePolicyById(Long policyId) {
        LeavePolicy policy = leavePolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave policy not found"));
        return convertToLeavePolicyResponse(policy);
    }

    @Override
    public LeavePolicyResponse updateLeavePolicy(Long policyId, UpdateLeavePolicyRequest request) {
        LeavePolicy policy = leavePolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave policy not found"));
        
        policy.setName(request.getName());
        policy.setDescription(request.getDescription());
        policy.setLeaveType(request.getLeaveType());
        policy.setTotalDaysPerYear(request.getTotalDaysPerYear());
        policy.setMaxConsecutiveDays(request.getMaxConsecutiveDays());
        policy.setMinServiceDaysRequired(request.getMinServiceDaysRequired());
        policy.setCarryForwardAllowed(request.getCarryForwardAllowed());
        policy.setMaxCarryForwardDays(request.getMaxCarryForwardDays());
        policy.setCarryForwardValidityMonths(request.getCarryForwardValidityMonths());
        policy.setEncashmentAllowed(request.getEncashmentAllowed());
        policy.setEncashmentPercentage(request.getEncashmentPercentage());
        policy.setMinDaysForEncashment(request.getMinDaysForEncashment());
        policy.setApprovalRequired(request.getApprovalRequired());
        policy.setMedicalCertificateRequiredDays(request.getMedicalCertificateRequiredDays());
        policy.setAdvanceNoticeDays(request.getAdvanceNoticeDays());
        policy.setRestrictApplyDuringProbation(request.getRestrictApplyDuringProbation());
        policy.setProbationMaxDays(request.getProbationMaxDays());
        policy.setApplicableRoles(request.getApplicableRoles());
        policy.setApplicableDepartmentIds(request.getApplicableDepartmentIds());
        policy.setUpdatedAt(LocalDateTime.now());
        
        LeavePolicy saved = leavePolicyRepository.save(policy);
        return convertToLeavePolicyResponse(saved);
    }

    @Override
    public void deleteLeavePolicy(Long policyId) {
        LeavePolicy policy = leavePolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave policy not found"));
        policy.setIsActive(false);
        policy.setUpdatedAt(LocalDateTime.now());
        leavePolicyRepository.save(policy);
    }

    // Dashboard
    @Override
    @Transactional(readOnly = true)
    public CompanyDashboardResponse getCompanyDashboard() {
        CompanyDashboardResponse response = new CompanyDashboardResponse();
        response.setTotalEmployees(employeeRepository.countActiveEmployeesByCompanyId(1L));
        response.setActiveEmployees(employeeRepository.countActiveEmployeesByCompanyId(1L));
        response.setTotalDepartments(departmentRepository.count());
        response.setPresentToday(0L);
        response.setAbsentToday(0L);
        response.setLateToday(0L);
        response.setOnLeaveToday(0L);
        response.setPendingLeaveRequests(0L);
        response.setApprovedLeavesThisMonth(0L);
        response.setEmployeesOnLeave(0L);
        response.setNewEmployeesThisMonth(0L);
        response.setAverageAttendanceRate(0.0);
        response.setTotalPayrollThisMonth(0.0);
        response.setAverageSalary(0.0);
        response.setPayrollProcessedThisMonth(0L);
        return response;
    }

    // Bulk Operations
    @Override
    public BulkImportResponse bulkImportEmployees(BulkImportRequest request) {
        BulkImportResponse response = new BulkImportResponse();
        response.setOperationType("BULK_IMPORT_EMPLOYEES");
        response.setTimestamp(LocalDateTime.now());
        response.setTotalRecords(0);
        response.setSuccessful(0);
        response.setFailed(0);
        response.setSkipped(0);
        response.setOverallSuccess(false);
        response.setMessage("Bulk import not implemented yet");
        response.setErrors(List.of("Bulk import not implemented yet"));
        return response;
    }

    @Override
    public BulkOperationResponse bulkAssignDepartment(BulkAssignDepartmentRequest request) {
        BulkOperationResponse response = new BulkOperationResponse();
        response.setOperationType("BULK_ASSIGN_DEPARTMENT");
        response.setTimestamp(LocalDateTime.now());
        response.setTotalRequested(request.getEmployeeIds().size());
        response.setSuccessful(0);
        response.setFailed(request.getEmployeeIds().size());
        response.setOverallSuccess(false);
        response.setMessage("Bulk department assignment not implemented yet");
        response.setErrors(List.of("Bulk department assignment not implemented yet"));
        return response;
    }

    @Override
    public BulkOperationResponse bulkAssignShift(BulkAssignShiftRequest request) {
        BulkOperationResponse response = new BulkOperationResponse();
        response.setOperationType("BULK_ASSIGN_SHIFT");
        response.setTimestamp(LocalDateTime.now());
        response.setTotalRequested(request.getEmployeeIds().size());
        response.setSuccessful(0);
        response.setFailed(request.getEmployeeIds().size());
        response.setOverallSuccess(false);
        response.setMessage("Bulk shift assignment not implemented yet");
        response.setErrors(List.of("Bulk shift assignment not implemented yet"));
        return response;
    }

    // Reports
    @Override
    public byte[] generateEmployeeReport(Long departmentId, String format) {
        return "Employee report content".getBytes();
    }

    @Override
    public byte[] generateAttendanceSummaryReport(String startDate, String endDate, String format) {
        return "Attendance summary report content".getBytes();
    }

    // Settings
    @Override
    @Transactional(readOnly = true)
    public CompanySettingsResponse getCompanySettings() {
        CompanySettingsResponse response = new CompanySettingsResponse();
        response.setGpsRequired(false);
        response.setFaceRecognitionRequired(false);
        response.setOfficeRadiusMeters(100);
        response.setOfficeLatitude(12.9716);
        response.setOfficeLongitude(77.5946);
        response.setGracePeriodMinutes(15);
        response.setLeaveApprovalRequired(true);
        response.setMaxCarryForwardDays(5);
        response.setLeaveEncashmentEnabled(true);
        response.setPayrollProcessingDay("25");
        response.setOvertimeCalculationEnabled(true);
        response.setOvertimeRate(1.5);
        response.setEmailNotificationsEnabled(true);
        response.setSmsNotificationsEnabled(false);
        response.setPushNotificationsEnabled(true);
        response.setTwoFactorAuthEnabled(false);
        response.setPasswordExpiryDays(90);
        response.setSessionTimeoutEnabled(true);
        response.setWorkingDays("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY");
        response.setWorkingHoursStart("09:00");
        response.setWorkingHoursEnd("18:00");
        response.setLunchBreakStart("13:00");
        response.setLunchBreakEnd("14:00");
        response.setLastUpdated(LocalDateTime.now());
        return response;
    }

    @Override
    public CompanySettingsResponse updateCompanySettings(UpdateCompanySettingsRequest request) {
        CompanySettingsResponse response = new CompanySettingsResponse();
        response.setGpsRequired(request.getGpsRequired());
        response.setFaceRecognitionRequired(request.getFaceRecognitionRequired());
        response.setOfficeRadiusMeters(request.getOfficeRadiusMeters());
        response.setOfficeLatitude(request.getOfficeLatitude());
        response.setOfficeLongitude(request.getOfficeLongitude());
        response.setGracePeriodMinutes(request.getGracePeriodMinutes());
        response.setLeaveApprovalRequired(request.getLeaveApprovalRequired());
        response.setMaxCarryForwardDays(request.getMaxCarryForwardDays());
        response.setLeaveEncashmentEnabled(request.getLeaveEncashmentEnabled());
        response.setPayrollProcessingDay(request.getPayrollProcessingDay());
        response.setOvertimeCalculationEnabled(request.getOvertimeCalculationEnabled());
        response.setOvertimeRate(request.getOvertimeRate());
        response.setEmailNotificationsEnabled(request.getEmailNotificationsEnabled());
        response.setSmsNotificationsEnabled(request.getSmsNotificationsEnabled());
        response.setPushNotificationsEnabled(request.getPushNotificationsEnabled());
        response.setTwoFactorAuthEnabled(request.getTwoFactorAuthEnabled());
        response.setPasswordExpiryDays(request.getPasswordExpiryDays());
        response.setSessionTimeoutEnabled(request.getSessionTimeoutEnabled());
        response.setWorkingDays(request.getWorkingDays());
        response.setWorkingHoursStart(request.getWorkingHoursStart());
        response.setWorkingHoursEnd(request.getWorkingHoursEnd());
        response.setLunchBreakStart(request.getLunchBreakStart());
        response.setLunchBreakEnd(request.getLunchBreakEnd());
        response.setLastUpdated(LocalDateTime.now());
        return response;
    }

    // Helper methods for conversion
    private DepartmentResponse convertToDepartmentResponse(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setName(department.getName());
        response.setDescription(department.getDescription());
        response.setHeadOfDepartmentId(department.getHeadOfDepartmentId());
        response.setParentDepartmentId(department.getParentDepartmentId());
        response.setBudgetLimit(department.getBudgetLimit());
        return response;
    }

    private EmployeeResponse convertToEmployeeResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setEmployeeCode(employee.getEmployeeCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setPhone(employee.getPhone());
        response.setDateOfBirth(employee.getDateOfBirth());
        response.setGender(employee.getGender());
        response.setDepartmentId(employee.getDepartmentId());
        response.setDesignation(employee.getDesignation());
        response.setEmploymentType(employee.getEmploymentType());
        response.setJoiningDate(employee.getJoiningDate());
        response.setSalary(employee.getSalary());
        response.setRole(employee.getRole());
        response.setStatus(employee.getStatus());
        return response;
    }

    private ShiftResponse convertToShiftResponse(Shift shift) {
        ShiftResponse response = new ShiftResponse();
        response.setId(shift.getId());
        response.setName(shift.getName());
        response.setDescription(shift.getDescription());
        response.setStartTime(shift.getStartTime());
        response.setEndTime(shift.getEndTime());
        response.setBreakStartTime(shift.getBreakStartTime());
        response.setBreakEndTime(shift.getBreakEndTime());
        response.setBreakDurationMinutes(shift.getBreakDurationMinutes());
        response.setGracePeriodMinutes(shift.getGracePeriodMinutes());
        response.setOvertimeThresholdMinutes(shift.getOvertimeThresholdMinutes());
        response.setHalfDayThresholdHours(shift.getHalfDayThresholdHours());
        response.setOfficeLatitude(shift.getOfficeLatitude());
        response.setOfficeLongitude(shift.getOfficeLongitude());
        response.setOfficeRadiusMeters(shift.getOfficeRadiusMeters());
        response.setIsGpsRequired(shift.getIsGpsRequired());
        response.setIsFaceRecognitionRequired(shift.getIsFaceRecognitionRequired());
        response.setIsWeekendWorkAllowed(shift.getIsWeekendWorkAllowed());
        response.setIsHolidayWorkAllowed(shift.getIsHolidayWorkAllowed());
        if (shift.getWorkingDays() != null) {
            response.setWorkingDays(shift.getWorkingDays().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList()));
        }
        return response;
    }

    private HolidayResponse convertToHolidayResponse(HolidayCalendar holiday) {
        HolidayResponse response = new HolidayResponse();
        response.setId(holiday.getId());
        response.setName(holiday.getName());
        response.setDate(holiday.getDate());
        response.setHolidayType(holiday.getHolidayType().toString());
        response.setDescription(holiday.getDescription());
        response.setIsRecurring(holiday.getIsRecurring());
        response.setIsOptional(holiday.getIsOptional());
        response.setIsHalfDay(holiday.getIsHalfDay());
        response.setApplicableToAll(holiday.getApplicableToAll());
        response.setApplicableDepartmentIds(holiday.getApplicableDepartmentIds());
        response.setApplicableEmployeeIds(holiday.getApplicableEmployeeIds());
        return response;
    }

    private LeavePolicyResponse convertToLeavePolicyResponse(LeavePolicy policy) {
        LeavePolicyResponse response = new LeavePolicyResponse();
        response.setId(policy.getId());
        response.setName(policy.getName());
        response.setDescription(policy.getDescription());
        response.setLeaveType(policy.getLeaveType());
        response.setTotalDaysPerYear(policy.getTotalDaysPerYear());
        response.setMaxConsecutiveDays(policy.getMaxConsecutiveDays());
        response.setMinServiceDaysRequired(policy.getMinServiceDaysRequired());
        response.setCarryForwardAllowed(policy.getCarryForwardAllowed());
        response.setMaxCarryForwardDays(policy.getMaxCarryForwardDays());
        response.setCarryForwardValidityMonths(policy.getCarryForwardValidityMonths());
        response.setEncashmentAllowed(policy.getEncashmentAllowed());
        response.setEncashmentPercentage(policy.getEncashmentPercentage());
        response.setMinDaysForEncashment(policy.getMinDaysForEncashment());
        response.setApprovalRequired(policy.getApprovalRequired());
        response.setMedicalCertificateRequiredDays(policy.getMedicalCertificateRequiredDays());
        response.setAdvanceNoticeDays(policy.getAdvanceNoticeDays());
        response.setRestrictApplyDuringProbation(policy.getRestrictApplyDuringProbation());
        response.setProbationMaxDays(policy.getProbationMaxDays());
        if (policy.getApplicableRoles() != null) {
            response.setApplicableRoles(policy.getApplicableRoles().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList()));
        }
        response.setApplicableDepartmentIds(policy.getApplicableDepartmentIds());
        return response;
    }

    private String generateEmployeeCode() {
        return "EMP" + System.currentTimeMillis();
    }
}
