package com.hr.demo.entity;
import com.hr.demo.domain.company.CompanyStatus;
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

    // Company basic info
    @Column(nullable = false)
    private String companyName;

    private String ownerName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String phone;

    // Subscription control
    private Integer employeeLimit;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status;

    private LocalDate subscriptionStart;
    private LocalDate subscriptionEnd;

    // 🔐 Multi Tenant Link (VERY IMPORTANT)
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserEntity> users;

    // 📊 Audit fields (important for SaaS)
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