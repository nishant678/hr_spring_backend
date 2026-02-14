package com.hr.demo.entity;

import com.hr.demo.enums.CompanyStatus;
import com.hr.demo.enums.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "companies", indexes = {
    @Index(name = "idx_company_status", columnList = "status"),
    @Index(name = "idx_company_subscription", columnList = "subscriptionEnd")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String postalCode;

    @Column(name = "company_code", unique = true, nullable = false, length = 10)
    private String companyCode;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "website")
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompanyStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_plan", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "subscription_start", nullable = false)
    private LocalDateTime subscriptionStart;

    @Column(name = "subscription_end", nullable = false)
    private LocalDateTime subscriptionEnd;

    @Column(name = "max_employees")
    private Integer maxEmployees;

    @Column(name = "current_employees", nullable = false)
    private Integer currentEmployees;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Department> departments;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Shift> shifts;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HolidayCalendar> holidayCalendars;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LeavePolicy> leavePolicies;

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
        if (companyCode == null) {
            companyCode = generateCompanyCode();
        }
        if (status == null) {
            status = CompanyStatus.PENDING;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (currentEmployees == null) {
            currentEmployees = 0;
        }
        if (subscriptionPlan == null) {
            subscriptionPlan = SubscriptionPlan.BASIC;
        }
    }

    private String generateCompanyCode() {
        return "CMP" + System.currentTimeMillis() % 100000;
    }
}
