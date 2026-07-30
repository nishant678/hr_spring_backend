package com.hr.demo.reaponse;

import com.hr.demo.util.NumberToWords;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class SalarySlipResponse {

    private final String employeeName;
    private final String employeeId;
    private final String designation;
    private final String department;
    private final int year;
    private final int month;
    private final String monthName;

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

    private final String bankName;
    private final String bankAccountNumber;
    private final String ifscCode;
    private final String panNumber;
    private final String uanNumber;

    // Constructor for live (on-the-fly) calculation from user entity
    public SalarySlipResponse(String employeeName, String employeeId, String designation,
                              String department, int year, int month,
                              BigDecimal basicSalary, BigDecimal grossSalary,
                              String bankName, String bankAccountNumber,
                              String ifscCode, String panNumber, String uanNumber) {
        this.employeeName = employeeName;
        this.employeeId = employeeId;
        this.designation = designation;
        this.department = department;
        this.year = year;
        this.month = month;
        this.monthName = getMonthName(month);

        this.basicSalary = basicSalary;
        this.hra = basicSalary.multiply(BigDecimal.valueOf(0.50)).setScale(2, RoundingMode.HALF_UP);
        this.conveyance = BigDecimal.valueOf(1600).setScale(2, RoundingMode.HALF_UP);
        this.medical = BigDecimal.valueOf(1250).setScale(2, RoundingMode.HALF_UP);
        this.specialAllowance = grossSalary.subtract(basicSalary).subtract(hra)
                .subtract(conveyance).subtract(medical)
                .max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        this.grossEarnings = grossSalary;

        this.pf = basicSalary.multiply(BigDecimal.valueOf(0.12)).setScale(2, RoundingMode.HALF_UP);
        this.esi = grossSalary.compareTo(BigDecimal.valueOf(21000)) <= 0
                ? grossSalary.multiply(BigDecimal.valueOf(0.0075)).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        this.professionalTax = BigDecimal.valueOf(200).setScale(2, RoundingMode.HALF_UP);
        this.tds = BigDecimal.ZERO;
        this.totalDeductions = pf.add(esi).add(professionalTax).add(tds);

        this.netPay = grossEarnings.subtract(totalDeductions).setScale(2, RoundingMode.HALF_UP);
        this.netPayInWords = "Rupees " + NumberToWords.convert(netPay.longValue()) + " Only";

        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.ifscCode = ifscCode;
        this.panNumber = panNumber;
        this.uanNumber = uanNumber;
    }

    // Constructor for processed payroll (pre-computed values)
    public SalarySlipResponse(String employeeName, String employeeId, String designation,
                              String department, int year, int month,
                              BigDecimal basicSalary, BigDecimal grossEarnings,
                              String bankName, String bankAccountNumber,
                              String ifscCode, String panNumber, String uanNumber,
                              BigDecimal hra, BigDecimal conveyance, BigDecimal medical,
                              BigDecimal specialAllowance,
                              BigDecimal pf, BigDecimal esi, BigDecimal professionalTax,
                              BigDecimal tds, BigDecimal totalDeductions,
                              BigDecimal netPay, String netPayInWords) {
        this.employeeName = employeeName;
        this.employeeId = employeeId;
        this.designation = designation;
        this.department = department;
        this.year = year;
        this.month = month;
        this.monthName = getMonthName(month);
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.conveyance = conveyance;
        this.medical = medical;
        this.specialAllowance = specialAllowance;
        this.grossEarnings = grossEarnings;
        this.pf = pf;
        this.esi = esi;
        this.professionalTax = professionalTax;
        this.tds = tds;
        this.totalDeductions = totalDeductions;
        this.netPay = netPay;
        this.netPayInWords = netPayInWords;
        this.bankName = bankName;
        this.bankAccountNumber = bankAccountNumber;
        this.ifscCode = ifscCode;
        this.panNumber = panNumber;
        this.uanNumber = uanNumber;
    }

    private String getMonthName(int m) {
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return (m >= 1 && m <= 12) ? months[m - 1] : "Unknown";
    }
}
