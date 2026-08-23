package com.hr.demo.reaponse;

import com.hr.demo.hr.entity.PayrollEntity;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PayrollResponse {

    private final Long id;
    private final Long userId;
    private final String employeeName;
    private final String employeeId;
    private final String designation;
    private final String department;
    private final int year;
    private final int month;
    private final String monthName;

    private final int totalWorkingDays;
    private final int presentDays;
    private final int lateDays;
    private final int halfDays;
    private final int absentDays;
    private final int paidLeaveDays;

    private final BigDecimal basicSalary;
    private final BigDecimal hra;
    private final BigDecimal conveyance;
    private final BigDecimal medical;
    private final BigDecimal specialAllowance;
    private final BigDecimal grossEarnings;

    private final BigDecimal pf;
    private final BigDecimal esi;
    private final BigDecimal professionalTax;
    private final BigDecimal tds;
    private final BigDecimal totalDeductions;

    private final BigDecimal netPay;
    private final String netPayInWords;

    private final String status;
    private final String processedDate;
    private final String paidDate;

    private final String bankName;
    private final String bankAccountNumber;
    private final String ifscCode;

    public PayrollResponse(PayrollEntity entity) {
        this.id = entity.getId();
        this.userId = entity.getUser().getId();
        String fn = entity.getUser().getFirstName() != null ? entity.getUser().getFirstName() : "";
        String ln = entity.getUser().getLastName() != null ? entity.getUser().getLastName() : "";
        this.employeeName = (fn + " " + ln).trim();
        this.employeeId = entity.getUser().getEmployeeId() != null ? entity.getUser().getEmployeeId() : "";
        this.designation = entity.getUser().getDesignation() != null
                ? entity.getUser().getDesignation().getName() : "";
        this.department = entity.getUser().getDepartment() != null
                ? entity.getUser().getDepartment().getName() : "";
        this.year = entity.getYear();
        this.month = entity.getMonth();
        this.monthName = entity.getMonthName();
        this.totalWorkingDays = entity.getTotalWorkingDays();
        this.presentDays = entity.getPresentDays();
        this.lateDays = entity.getLateDays();
        this.halfDays = entity.getHalfDays();
        this.absentDays = entity.getAbsentDays();
        this.paidLeaveDays = entity.getPaidLeaveDays();
        this.basicSalary = entity.getBasicSalary();
        this.hra = entity.getHra();
        this.conveyance = entity.getConveyance();
        this.medical = entity.getMedical();
        this.specialAllowance = entity.getSpecialAllowance();
        this.grossEarnings = entity.getGrossEarnings();
        this.pf = entity.getPf();
        this.esi = entity.getEsi();
        this.professionalTax = entity.getProfessionalTax();
        this.tds = entity.getTds();
        this.totalDeductions = entity.getTotalDeductions();
        this.netPay = entity.getNetPay();
        this.netPayInWords = entity.getNetPayInWords();
        this.status = entity.getStatus();
        this.processedDate = entity.getProcessedDate() != null ? entity.getProcessedDate().toString() : null;
        this.paidDate = entity.getPaidDate() != null ? entity.getPaidDate().toString() : null;
        this.bankName = entity.getBankName();
        this.bankAccountNumber = entity.getBankAccountNumber();
        this.ifscCode = entity.getIfscCode();
    }
}
