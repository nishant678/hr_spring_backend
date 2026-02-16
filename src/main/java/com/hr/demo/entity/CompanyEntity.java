package com.hr.demo.entity;
import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.domain.user.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // BASIC INFO
    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String ownerName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    private String website;
    private String logoUrl;

    // ADDRESS
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    // LEGAL
    private String gstNumber;
    private String panNumber;

    // SUBSCRIPTION
    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;

    private Integer employeeLimit;

    private LocalDate subscriptionStart;
    private LocalDate subscriptionEnd;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status;

    // MULTI TENANT RELATION
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<UserEntity> users;

    // AUDIT
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (status == null)
            status = CompanyStatus.ACTIVE;

        if (subscriptionStart == null)
            subscriptionStart = LocalDate.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}