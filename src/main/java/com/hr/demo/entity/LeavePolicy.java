package com.hr.demo.entity;

import com.hr.demo.enums.EmployeeRole;
import com.hr.demo.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "leave_policies", indexes = {
    @Index(name = "idx_leave_policy_company", columnList = "companyId"),
    @Index(name = "idx_leave_policy_type", columnList = "leaveType"),
    @Index(name = "idx_leave_policy_status", columnList = "status")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeavePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "policy_code", unique = true, nullable = false, length = 20)
    private String policyCode;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;

    @Column(name = "total_days_per_year", nullable = false)
    private Integer totalDaysPerYear;

    @Column(name = "max_consecutive_days")
    private Integer maxConsecutiveDays;

    @Column(name = "min_service_days_required")
    private Integer minServiceDaysRequired;

    @Column(name = "carry_forward_allowed", nullable = false)
    private Boolean carryForwardAllowed;

    @Column(name = "max_carry_forward_days")
    private Integer maxCarryForwardDays;

    @Column(name = "carry_forward_validity_months")
    private Integer carryForwardValidityMonths;

    @Column(name = "encashment_allowed", nullable = false)
    private Boolean encashmentAllowed;

    @Column(name = "encashment_percentage")
    private Double encashmentPercentage;

    @Column(name = "min_days_for_encashment")
    private Integer minDaysForEncashment;

    @Column(name = "approval_required", nullable = false)
    private Boolean approvalRequired;

    @Column(name = "medical_certificate_required_days")
    private Integer medicalCertificateRequiredDays;

    @Column(name = "advance_notice_days")
    private Integer advanceNoticeDays;

    @Column(name = "restrict_apply_during_probation", nullable = false)
    private Boolean restrictApplyDuringProbation;

    @Column(name = "probation_max_days")
    private Integer probationMaxDays;

    @ElementCollection
    @CollectionTable(name = "leave_policy_applicable_roles", joinColumns = @JoinColumn(name = "policy_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<EmployeeRole> applicableRoles;

    @ElementCollection
    @CollectionTable(name = "leave_policy_applicable_departments", joinColumns = @JoinColumn(name = "policy_id"))
    @Column(name = "department_id")
    private List<Long> applicableDepartmentIds;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "effective_from", nullable = false)
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

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
        if (policyCode == null) {
            policyCode = generatePolicyCode();
        }
        if (isActive == null) {
            isActive = true;
        }
        if (carryForwardAllowed == null) {
            carryForwardAllowed = false;
        }
        if (encashmentAllowed == null) {
            encashmentAllowed = false;
        }
        if (approvalRequired == null) {
            approvalRequired = true;
        }
        if (restrictApplyDuringProbation == null) {
            restrictApplyDuringProbation = false;
        }
        if (effectiveFrom == null) {
            effectiveFrom = LocalDateTime.now();
        }
    }

    private String generatePolicyCode() {
        return "LVP" + System.currentTimeMillis() % 100000;
    }
}
