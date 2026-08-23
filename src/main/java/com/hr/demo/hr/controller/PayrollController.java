package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.PayrollResponse;
import com.hr.demo.reaponse.PayrollSummaryResponse;
import com.hr.demo.hr.service.PayrollService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;
    private final SecurityUtil securityUtil;

    private UserEntity getCurrentUser() {
        return securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
    }

    private Long currentCompanyId() {
        UserEntity user = getCurrentUser();
        if (user.getCompany() == null) {
            throw new UnauthorizedException("User not linked to a company");
        }
        return user.getCompany().getId();
    }

    @PostMapping("/api/payroll/process")
    public ResponseEntity<ApiResponse<List<PayrollResponse>>> processPayroll(
            @RequestParam int year, @RequestParam int month) {
        Long companyId = currentCompanyId();
        List<PayrollResponse> records = payrollService.processPayroll(companyId, year, month);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payroll processed for " + records.size() + " employees", records));
    }

    @GetMapping("/api/payroll")
    public ResponseEntity<ApiResponse<List<PayrollResponse>>> getPayrollRecords(
            @RequestParam int year, @RequestParam int month) {
        Long companyId = currentCompanyId();
        List<PayrollResponse> records = payrollService.getPayrollRecords(companyId, year, month);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payroll records fetched", records));
    }

    @GetMapping("/api/payroll/summary")
    public ResponseEntity<ApiResponse<PayrollSummaryResponse>> getPayrollSummary(
            @RequestParam int year, @RequestParam int month) {
        Long companyId = currentCompanyId();
        PayrollSummaryResponse summary = payrollService.getSummary(companyId, year, month);
        return ResponseEntity.ok(new ApiResponse<>(true, "Payroll summary fetched", summary));
    }

    @GetMapping("/api/payroll/{id}")
    public ResponseEntity<ApiResponse<PayrollResponse>> getPayrollRecord(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Payroll record fetched", payrollService.getPayrollRecord(id)));
    }

    @GetMapping("/api/payroll/my")
    public ResponseEntity<ApiResponse<PayrollResponse>> getMyPayroll(
            @RequestParam int year, @RequestParam int month) {
        UserEntity user = getCurrentUser();
        PayrollResponse record = payrollService.getMyPayrollRecord(user.getId(), year, month);
        if (record == null) {
            return ResponseEntity.ok(new ApiResponse<>(false, "No payroll record found for this month", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Payroll record fetched", record));
    }

    @PutMapping("/api/payroll/{id}/pay")
    public ResponseEntity<ApiResponse<PayrollResponse>> markAsPaid(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Payroll marked as paid", payrollService.markAsPaid(id)));
    }
}
