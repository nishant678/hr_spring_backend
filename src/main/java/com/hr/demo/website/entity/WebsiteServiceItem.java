package com.hr.demo.website.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "website_services",
    indexes = {
        @Index(name = "idx_website_services_active_order", columnList = "active, sort_order")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsiteServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(nullable = false, length = 80)
    private String icon;

    @Column(nullable = false, length = 140)
    private String title;

    @Column(length = 180)
    private String subtitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
