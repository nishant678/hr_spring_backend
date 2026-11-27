package com.hr.demo.website.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "website_job_applications",
    indexes = {
        @Index(name = "idx_website_apps_status_applied", columnList = "status, applied_at"),
        @Index(name = "idx_website_apps_email", columnList = "email")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "job_title", nullable = false, length = 160)
    private String jobTitle;

    @Column(name = "applicant_name", nullable = false, length = 160)
    private String applicantName;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(length = 40)
    private String phone;

    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;

    @Column(name = "cover_note", columnDefinition = "TEXT")
    private String coverNote;

    @Column(name = "resume_path", length = 255)
    private String resumePath;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "RECEIVED";

    @Column(name = "applied_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime appliedAt = LocalDateTime.now();
}
