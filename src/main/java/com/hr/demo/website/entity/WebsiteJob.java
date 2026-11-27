package com.hr.demo.website.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "website_jobs",
    indexes = {
        @Index(name = "idx_website_jobs_active_created", columnList = "active, created_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(nullable = false, length = 120)
    private String location;

    @Column(name = "employment_type", nullable = false, length = 60)
    private String employmentType;

    @Column(name = "salary_range", nullable = false, length = 80)
    private String salaryRange;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
