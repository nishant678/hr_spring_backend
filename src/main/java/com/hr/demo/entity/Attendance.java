package com.hr.demo.entity;

import com.hr.demo.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance", indexes = {
    @Index(name = "idx_attendance_employee", columnList = "employeeId"),
    @Index(name = "idx_attendance_date", columnList = "date"),
    @Index(name = "idx_attendance_company_date", columnList = "companyId, date"),
    @Index(name = "idx_attendance_status", columnList = "status")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Column(name = "check_in_location")
    private String checkInLocation;

    @Column(name = "check_out_location")
    private String checkOutLocation;

    @Column(name = "check_in_latitude")
    private Double checkInLatitude;

    @Column(name = "check_in_longitude")
    private Double checkInLongitude;

    @Column(name = "check_out_latitude")
    private Double checkOutLatitude;

    @Column(name = "check_out_longitude")
    private Double checkOutLongitude;

    @Column(name = "check_in_device_id")
    private String checkInDeviceId;

    @Column(name = "check_out_device_id")
    private String checkOutDeviceId;

    @Column(name = "check_in_selfie")
    private String checkInSelfie;

    @Column(name = "check_out_selfie")
    private String checkOutSelfie;

    @Column(name = "face_recognition_score")
    private Double faceRecognitionScore;

    @Column(name = "total_hours")
    private Double totalHours;

    @Column(name = "overtime_hours")
    private Double overtimeHours;

    @Column(name = "late_minutes")
    private Integer lateMinutes;

    @Column(name = "early_departure_minutes")
    private Integer earlyDepartureMinutes;

    @Column(name = "shift_id")
    private Long shiftId;

    @Column(name = "shift_start")
    private LocalDateTime shiftStart;

    @Column(name = "shift_end")
    private LocalDateTime shiftEnd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @Column(name = "is_gps_verified", nullable = false)
    private Boolean isGpsVerified;

    @Column(name = "is_face_verified", nullable = false)
    private Boolean isFaceVerified;

    @Column(name = "is_manual_entry", nullable = false)
    private Boolean isManualEntry;

    @Column(name = "manual_entry_reason")
    private String manualEntryReason;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "remarks")
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", insertable = false, updatable = false)
    private Shift shift;

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
        if (status == null) {
            status = AttendanceStatus.PRESENT;
        }
        if (isGpsVerified == null) {
            isGpsVerified = false;
        }
        if (isFaceVerified == null) {
            isFaceVerified = false;
        }
        if (isManualEntry == null) {
            isManualEntry = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        calculateTotalHours();
        calculateLateMinutes();
        calculateOvertimeHours();
    }

    private void calculateTotalHours() {
        if (checkIn != null && checkOut != null) {
            totalHours = java.time.Duration.between(checkIn, checkOut).toMinutes() / 60.0;
        }
    }

    private void calculateLateMinutes() {
        if (checkIn != null && shiftStart != null) {
            if (checkIn.isAfter(shiftStart)) {
                lateMinutes = (int) java.time.Duration.between(shiftStart, checkIn).toMinutes();
            } else {
                lateMinutes = 0;
            }
        }
    }

    private void calculateOvertimeHours() {
        if (checkOut != null && shiftEnd != null) {
            if (checkOut.isAfter(shiftEnd)) {
                overtimeHours = java.time.Duration.between(shiftEnd, checkOut).toMinutes() / 60.0;
            } else {
                overtimeHours = 0.0;
            }
        }
    }
}
