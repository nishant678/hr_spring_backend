package com.hr.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    private String monthName;

    // Attendance summary
    private int totalWorkingDays;
    private int presentDays;
    private int lateDays;
    private int halfDays;
    private int absentDays;
    private int paidLeaveDays;
    private int unpaidLeaveDays;

    // Earnings
    private BigDecimal basicSalary;
    private BigDecimal hra;
    private BigDecimal conveyance;
    private BigDecimal medical;
    private BigDecimal specialAllowance;
    private BigDecimal grossEarnings;

    // Deductions
    private BigDecimal pf;
    private BigDecimal esi;
    private BigDecimal professionalTax;
    private BigDecimal tds;
    private BigDecimal totalDeductions;

    // Net
    private BigDecimal netPay;
    private String netPayInWords;

    // Status: PROCESSED, PAID
    private String status;

    private LocalDate processedDate;
    private LocalDate paidDate;

    // Bank snapshot
    private String bankName;
    private String bankAccountNumber;
    private String ifscCode;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
