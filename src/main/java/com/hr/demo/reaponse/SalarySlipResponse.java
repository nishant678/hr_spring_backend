package com.hr.demo.reaponse;

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
        this.netPayInWords = "Rupees " + numberToWords(netPay.longValue()) + " Only";

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

    private String numberToWords(long n) {
        if (n == 0) return "Zero";
        String[] ones = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
                "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
                "Seventeen", "Eighteen", "Nineteen"};
        String[] tens = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
        if (n < 20) return ones[(int) n];
        if (n < 100) return tens[(int) (n / 10)] + (n % 10 > 0 ? " " + ones[(int) (n % 10)] : "");
        if (n < 1000) return ones[(int) (n / 100)] + " Hundred" + (n % 100 > 0 ? " " + numberToWords(n % 100) : "");
        if (n < 100000) return numberToWords(n / 1000) + " Thousand" + (n % 1000 > 0 ? " " + numberToWords(n % 1000) : "");
        if (n < 10000000) return numberToWords(n / 100000) + " Lakh" + (n % 100000 > 0 ? " " + numberToWords(n % 100000) : "");
        return numberToWords(n / 10000000) + " Crore" + (n % 10000000 > 0 ? " " + numberToWords(n % 10000000) : "");
    }
}
