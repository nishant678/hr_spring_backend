package com.hr.demo.entity;

import com.hr.demo.enums.DepartmentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "departments", indexes = {
    @Index(name = "idx_department_company", columnList = "companyId"),
    @Index(name = "idx_department_name", columnList = "name"),
    @Index(name = "idx_department_status", columnList = "status")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "department_code", unique = true, nullable = false, length = 20)
    private String departmentCode;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "head_of_department_id")
    private Long headOfDepartmentId;

    @Column(name = "parent_department_id")
    private Long parentDepartmentId;

    @Column(name = "total_employees", nullable = false)
    private Integer totalEmployees;

    @Column(name = "budget_limit")
    private Double budgetLimit;

    @Column(name = "current_budget")
    private Double currentBudget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepartmentStatus status;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_of_department_id", insertable = false, updatable = false)
    private Employee headOfDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_department_id", insertable = false, updatable = false)
    private Department parentDepartment;

    @OneToMany(mappedBy = "parentDepartment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Department> subDepartments;

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
        if (departmentCode == null) {
            departmentCode = generateDepartmentCode();
        }
        if (status == null) {
            status = DepartmentStatus.ACTIVE;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (totalEmployees == null) {
            totalEmployees = 0;
        }
    }

    private String generateDepartmentCode() {
        return "DEPT" + System.currentTimeMillis() % 100000;
    }
}
