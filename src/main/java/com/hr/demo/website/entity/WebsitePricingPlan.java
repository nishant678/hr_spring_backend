package com.hr.demo.website.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "website_pricing_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebsitePricingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "price_label", nullable = false, length = 40)
    private String priceLabel;

    @Column(name = "period_label", nullable = false, length = 40)
    private String periodLabel;

    @Column(nullable = false)
    @Builder.Default
    private Boolean popular = false;

    @Column(name = "features_csv", nullable = false, columnDefinition = "TEXT")
    private String featuresCsv;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
