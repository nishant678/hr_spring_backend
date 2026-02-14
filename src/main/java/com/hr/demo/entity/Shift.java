package com.hr.demo.entity;

import com.hr.demo.enums.ShiftStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "shifts", indexes = {
    @Index(name = "idx_shift_company", columnList = "companyId"),
    @Index(name = "idx_shift_name", columnList = "name"),
    @Index(name = "idx_shift_status", columnList = "status")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "shift_code", unique = true, nullable = false, length = 20)
    private String shiftCode;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "break_start_time")
    private LocalTime breakStartTime;

    @Column(name = "break_end_time")
    private LocalTime breakEndTime;

    @Column(name = "break_duration_minutes")
    private Integer breakDurationMinutes;

    @Column(name = "grace_period_minutes", nullable = false)
    private Integer gracePeriodMinutes;

    @Column(name = "overtime_threshold_minutes")
    private Integer overtimeThresholdMinutes;

    @Column(name = "half_day_threshold_hours")
    private Double halfDayThresholdHours;

    @Column(name = "late_threshold_minutes")
    private Integer lateThresholdMinutes;

    @Column(name = "early_departure_threshold_minutes")
    private Integer earlyDepartureThresholdMinutes;

    @Column(name = "office_latitude")
    private Double officeLatitude;

    @Column(name = "office_longitude")
    private Double officeLongitude;

    @Column(name = "office_radius_meters")
    private Integer officeRadiusMeters;

    @Column(name = "is_gps_required", nullable = false)
    private Boolean isGpsRequired;

    @Column(name = "is_face_recognition_required", nullable = false)
    private Boolean isFaceRecognitionRequired;

    @Column(name = "is_weekend_work_allowed", nullable = false)
    private Boolean isWeekendWorkAllowed;

    @Column(name = "is_holiday_work_allowed", nullable = false)
    private Boolean isHolidayWorkAllowed;

    @ElementCollection
    @CollectionTable(name = "shift_working_days", joinColumns = @JoinColumn(name = "shift_id"))
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> workingDays;

    @Column(name = "total_employees", nullable = false)
    private Integer totalEmployees;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShiftStatus status;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attendance> attendances;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;

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
        if (shiftCode == null) {
            shiftCode = generateShiftCode();
        }
        if (status == null) {
            status = ShiftStatus.ACTIVE;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (totalEmployees == null) {
            totalEmployees = 0;
        }
        if (gracePeriodMinutes == null) {
            gracePeriodMinutes = 15;
        }
        if (overtimeThresholdMinutes == null) {
            overtimeThresholdMinutes = 60;
        }
        if (halfDayThresholdHours == null) {
            halfDayThresholdHours = 4.0;
        }
        if (lateThresholdMinutes == null) {
            lateThresholdMinutes = 10;
        }
        if (earlyDepartureThresholdMinutes == null) {
            earlyDepartureThresholdMinutes = 30;
        }
        if (officeRadiusMeters == null) {
            officeRadiusMeters = 100;
        }
        if (isGpsRequired == null) {
            isGpsRequired = true;
        }
        if (isFaceRecognitionRequired == null) {
            isFaceRecognitionRequired = false;
        }
        if (isWeekendWorkAllowed == null) {
            isWeekendWorkAllowed = false;
        }
        if (isHolidayWorkAllowed == null) {
            isHolidayWorkAllowed = false;
        }
    }

    private String generateShiftCode() {
        return "SHFT" + System.currentTimeMillis() % 100000;
    }

    public Double getWorkingHours() {
        if (startTime != null && endTime != null) {
            long minutes = java.time.Duration.between(startTime, endTime).toMinutes();
            if (breakDurationMinutes != null) {
                minutes -= breakDurationMinutes;
            }
            return minutes / 60.0;
        }
        return 0.0;
    }
}
