package com.hr.demo.reaponse;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PayrollSummaryResponse {
    private long totalEmployees;
    private int processedCount;
    private int paidCount;
    private BigDecimal totalGrossSalary;
    private BigDecimal totalDeductions;
    private BigDecimal totalNetPay;
}
