package com.hr.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "face_profile")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FaceProfileEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, length = 100)
    private String employeeId;

    @Column(columnDefinition = "vector(512)")
    private double[] embedding;

    @Column(name = "model_name", nullable = false, length = 100)
    @Builder.Default
    private String modelName = "buffalo_l";

    @Column(name = "model_version", nullable = false, length = 20)
    @Builder.Default
    private String modelVersion = "v1";

    @Column(name = "quality_score")
    private Double qualityScore;

    @Column(name = "liveness_score")
    private Double livenessScore;

    @Builder.Default
    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist void prePersist() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate void preUpdate() { updatedAt = LocalDateTime.now(); }
}
