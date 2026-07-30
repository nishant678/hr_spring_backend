package com.hr.demo.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.SalarySlipResponse;
import com.hr.demo.service.PayrollService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class SalarySlipController {

    private final SecurityUtil securityUtil;
    private final PayrollService payrollService;

    private UserEntity getCurrentUser() {
        return securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
    }

    @GetMapping("/api/payslip")
    public ResponseEntity<ApiResponse<SalarySlipResponse>> getPayslip(
            @RequestParam int year,
            @RequestParam int month) {
        UserEntity user = getCurrentUser();

        // Try payroll record first
        var payrollRecord = payrollService.getMyPayrollRecord(user.getId(), year, month);
        if (payrollRecord != null) {
            SalarySlipResponse slip = new SalarySlipResponse(
                    payrollRecord.getEmployeeName(),
                    payrollRecord.getEmployeeId(),
                    payrollRecord.getDesignation(),
                    payrollRecord.getDepartment(),
                    year, month,
                    payrollRecord.getBasicSalary(),
                    payrollRecord.getGrossEarnings(),
                    payrollRecord.getBankName(),
                    payrollRecord.getBankAccountNumber(),
                    payrollRecord.getIfscCode(),
                    user.getPanNumber() != null ? user.getPanNumber() : "",
                    user.getUanNumber() != null ? user.getUanNumber() : "",
                    payrollRecord.getHra(),
                    payrollRecord.getConveyance(),
                    payrollRecord.getMedical(),
                    payrollRecord.getSpecialAllowance(),
                    payrollRecord.getPf(),
                    payrollRecord.getEsi(),
                    payrollRecord.getProfessionalTax(),
                    payrollRecord.getTds(),
                    payrollRecord.getTotalDeductions(),
                    payrollRecord.getNetPay(),
                    payrollRecord.getNetPayInWords());
            return ResponseEntity.ok(new ApiResponse<>(true, "Payslip fetched (processed)", slip));
        }

        // Fallback: live calculation from user entity
        String deptName = user.getDepartment() != null ? user.getDepartment().getName() : "";
        String desigName = user.getDesignation() != null ? user.getDesignation().getName() : "";
        String fullName = (user.getFirstName() != null ? user.getFirstName() : "")
                + " " + (user.getLastName() != null ? user.getLastName() : "");

        BigDecimal basic = user.getBasicSalary() != null ? user.getBasicSalary() : BigDecimal.ZERO;
        BigDecimal gross = user.getGrossSalary() != null ? user.getGrossSalary() : BigDecimal.ZERO;
        String bank = user.getBankName() != null ? user.getBankName() : "";
        String acct = user.getBankAccountNumber() != null ? user.getBankAccountNumber() : "";
        String ifsc = user.getIfscCode() != null ? user.getIfscCode() : "";
        String pan = user.getPanNumber() != null ? user.getPanNumber() : "";
        String uan = user.getUanNumber() != null ? user.getUanNumber() : "";

        SalarySlipResponse slip = new SalarySlipResponse(
                fullName.trim(), user.getEmployeeId(), desigName, deptName,
                year, month, basic, gross, bank, acct, ifsc, pan, uan);

        return ResponseEntity.ok(new ApiResponse<>(true, "Payslip fetched (estimated)", slip));
    }
}
