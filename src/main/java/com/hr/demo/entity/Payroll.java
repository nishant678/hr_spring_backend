package com.hr.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll", indexes = {
    @Index(name = "idx_payroll_employee", columnList = "employeeId"),
    @Index(name = "idx_payroll_company", columnList = "companyId"),
    @Index(name = "idx_payroll_month", columnList = "payrollMonth, payrollYear"),
    @Index(name = "idx_payroll_status", columnList = "status")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "payroll_code", unique = true, nullable = false, length = 20)
    private String payrollCode;

    @Column(name = "payroll_month", nullable = false)
    private Integer payrollMonth;

    @Column(name = "payroll_year", nullable = false)
    private Integer payrollYear;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "basic_salary", nullable = false)
    private Double basicSalary;

    @Column(name = "house_rent_allowance")
    private Double houseRentAllowance;

    @Column(name = "dearness_allowance")
    private Double dearnessAllowance;

    @Column(name = "travel_allowance")
    private Double travelAllowance;

    @Column(name = "medical_allowance")
    private Double medicalAllowance;

    @Column(name = "special_allowance")
    private Double specialAllowance;

    @Column(name = "performance_bonus")
    private Double performanceBonus;

    @Column(name = "overtime_payment")
    private Double overtimePayment;

    @Column(name = "arrear_payment")
    private Double arrearPayment;

    @Column(name = "total_earnings", nullable = false)
    private Double totalEarnings;

    @Column(name = "provident_fund")
    private Double providentFund;

    @Column(name = "professional_tax")
    private Double professionalTax;

    @Column(name = "income_tax")
    private Double incomeTax;

    @Column(name = "employee_state_insurance")
    private Double employeeStateInsurance;

    @Column(name = "leave_deduction")
    private Double leaveDeduction;

    @Column(name = "late_deduction")
    private Double lateDeduction;

    @Column(name = "other_deductions")
    private Double otherDeductions;

    @Column(name = "total_deductions", nullable = false)
    private Double totalDeductions;

    @Column(name = "net_salary", nullable = false)
    private Double netSalary;

    @Column(name = "total_working_days", nullable = false)
    private Integer totalWorkingDays;

    @Column(name = "present_days", nullable = false)
    private Integer presentDays;

    @Column(name = "leave_days", nullable = false)
    private Integer leaveDays;

    @Column(name = "absent_days", nullable = false)
    private Integer absentDays;

    @Column(name = "late_days", nullable = false)
    private Integer lateDays;

    @Column(name = "half_days", nullable = false)
    private Integer halfDays;

    @Column(name = "overtime_hours", nullable = false)
    private Double overtimeHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayrollStatus status;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "bank_ifsc")
    private String bankIfsc;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_remarks", columnDefinition = "TEXT")
    private String paymentRemarks;

    @Column(name = "generated_by")
    private Long generatedBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "processed_by")
    private Long processedBy;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @PrePersist
    protected void onCreate() {
        if (payrollCode == null) {
            payrollCode = generatePayrollCode();
        }
        if (status == null) {
            status = PayrollStatus.DRAFT;
        }
        calculateTotals();
    }

    @PreUpdate
    protected void onUpdate() {
        calculateTotals();
    }

    private String generatePayrollCode() {
        return "PR" + System.currentTimeMillis() % 100000;
    }

    private void calculateTotals() {
        // Calculate total earnings
        totalEarnings = (basicSalary != null ? basicSalary : 0.0) +
                (houseRentAllowance != null ? houseRentAllowance : 0.0) +
                (dearnessAllowance != null ? dearnessAllowance : 0.0) +
                (travelAllowance != null ? travelAllowance : 0.0) +
                (medicalAllowance != null ? medicalAllowance : 0.0) +
                (specialAllowance != null ? specialAllowance : 0.0) +
                (performanceBonus != null ? performanceBonus : 0.0) +
                (overtimePayment != null ? overtimePayment : 0.0) +
                (arrearPayment != null ? arrearPayment : 0.0);

        // Calculate total deductions
        totalDeductions = (providentFund != null ? providentFund : 0.0) +
                (professionalTax != null ? professionalTax : 0.0) +
                (incomeTax != null ? incomeTax : 0.0) +
                (employeeStateInsurance != null ? employeeStateInsurance : 0.0) +
                (leaveDeduction != null ? leaveDeduction : 0.0) +
                (lateDeduction != null ? lateDeduction : 0.0) +
                (otherDeductions != null ? otherDeductions : 0.0);

        // Calculate net salary
        netSalary = totalEarnings - totalDeductions;
    }
}

enum PayrollStatus {
    DRAFT, PENDING_APPROVAL, APPROVED, PROCESSED, PAID, CANCELLED
}
