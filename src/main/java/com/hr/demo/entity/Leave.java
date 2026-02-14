package com.hr.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leaves", indexes = {
    @Index(name = "idx_leave_employee", columnList = "employeeId"),
    @Index(name = "idx_leave_company", columnList = "companyId"),
    @Index(name = "idx_leave_dates", columnList = "fromDate, toDate"),
    @Index(name = "idx_leave_status", columnList = "status"),
    @Index(name = "idx_leave_type", columnList = "leaveType")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "leave_code", unique = true, nullable = false, length = 20)
    private String leaveCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    @Column(name = "total_days", nullable = false)
    private Double totalDays;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "attachment_url")
    private String attachmentUrl;

    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Column(name = "work_handover_to")
    private Long workHandoverTo;

    @Column(name = "work_handover_details", columnDefinition = "TEXT")
    private String workHandoverDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "rejected_by")
    private Long rejectedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "approval_remarks", columnDefinition = "TEXT")
    private String approvalRemarks;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "balance_before", nullable = false)
    private Double balanceBefore;

    @Column(name = "balance_after", nullable = false)
    private Double balanceAfter;

    @Column(name = "is_paid_leave", nullable = false)
    private Boolean isPaidLeave;

    @Column(name = "is_half_day", nullable = false)
    private Boolean isHalfDay;

    @Column(name = "half_day_session")
    private String halfDaySession;

    @Column(name = "applied_device")
    private String appliedDevice;

    @Column(name = "applied_ip")
    private String appliedIp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_handover_to", insertable = false, updatable = false)
    private Employee workHandoverEmployee;

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
        if (leaveCode == null) {
            leaveCode = generateLeaveCode();
        }
        if (status == null) {
            status = LeaveStatus.PENDING;
        }
        if (totalDays == null) {
            calculateTotalDays();
        }
        if (isPaidLeave == null) {
            isPaidLeave = true;
        }
        if (isHalfDay == null) {
            isHalfDay = false;
        }
    }

    private String generateLeaveCode() {
        return "LV" + System.currentTimeMillis() % 100000;
    }

    private void calculateTotalDays() {
        if (fromDate != null && toDate != null) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(fromDate, toDate) + 1;
            totalDays = isHalfDay ? 0.5 : (double) days;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        calculateTotalDays();
    }
}

enum LeaveType {
    SICK_LEAVE, CASUAL_LEAVE, EARNED_LEAVE, MATERNITY_LEAVE, PATERNITY_LEAVE, 
    COMPENSATORY_OFF, UNPAID_LEAVE, MARRIAGE_LEAVE, BEREAVEMENT_LEAVE, STUDY_LEAVE
}

enum LeaveStatus {
    PENDING, APPROVED, REJECTED, CANCELLED, WITHDRAWN
}
