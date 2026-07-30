package com.hr.demo.service;

import com.hr.demo.entity.PayrollEntity;
import com.hr.demo.reaponse.PayrollResponse;
import com.hr.demo.reaponse.PayrollSummaryResponse;

import java.util.List;

public interface PayrollService {

    List<PayrollResponse> processPayroll(Long companyId, int year, int month);

    List<PayrollResponse> getPayrollRecords(Long companyId, int year, int month);

    PayrollResponse getPayrollRecord(Long payrollId);

    PayrollResponse getMyPayrollRecord(Long userId, int year, int month);

    PayrollResponse markAsPaid(Long payrollId);

    PayrollSummaryResponse getSummary(Long companyId, int year, int month);
}
