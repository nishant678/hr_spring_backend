package com.hr.demo.website.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "website_contact_inquiries",
    indexes = {
        @Index(name = "idx_website_inquiries_status_created", columnList = "status, created_at"),
        @Index(name = "idx_website_inquiries_email", columnList = "email")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(length = 160)
    private String company;

    @Column(length = 40)
    private String phone;

    @Column(name = "service_interest", nullable = false, length = 120)
    private String serviceInterest;

    @Column(name = "selected_plan", length = 80)
    private String selectedPlan;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "source_page", length = 80)
    @Builder.Default
    private String sourcePage = "contact";

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "NEW";

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
