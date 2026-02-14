package com.hr.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "holiday_calendars", indexes = {
    @Index(name = "idx_holiday_company", columnList = "companyId"),
    @Index(name = "idx_holiday_date", columnList = "date"),
    @Index(name = "idx_holiday_company_date", columnList = "companyId, date"),
    @Index(name = "idx_holiday_type", columnList = "holidayType")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HolidayCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "holiday_code", unique = true, nullable = false, length = 20)
    private String holidayCode;

    @Column(nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "holiday_type", nullable = false)
    private HolidayType holidayType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_recurring", nullable = false)
    private Boolean isRecurring;

    @Column(name = "is_optional", nullable = false)
    private Boolean isOptional;

    @Column(name = "is_half_day", nullable = false)
    private Boolean isHalfDay;

    @Column(name = "applicable_to_all", nullable = false)
    private Boolean applicableToAll;

    @ElementCollection
    @CollectionTable(name = "holiday_applicable_departments", joinColumns = @JoinColumn(name = "holiday_id"))
    @Column(name = "department_id")
    private java.util.List<Long> applicableDepartmentIds;

    @ElementCollection
    @CollectionTable(name = "holiday_applicable_employees", joinColumns = @JoinColumn(name = "holiday_id"))
    @Column(name = "employee_id")
    private java.util.List<Long> applicableEmployeeIds;

    @Column(name = "notification_sent", nullable = false)
    private Boolean notificationSent;

    @Column(name = "notification_sent_at")
    private LocalDateTime notificationSentAt;

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
        if (holidayCode == null) {
            holidayCode = generateHolidayCode();
        }
        if (dayOfWeek == null && date != null) {
            dayOfWeek = date.getDayOfWeek().toString();
        }
        if (isRecurring == null) {
            isRecurring = false;
        }
        if (isOptional == null) {
            isOptional = false;
        }
        if (isHalfDay == null) {
            isHalfDay = false;
        }
        if (applicableToAll == null) {
            applicableToAll = true;
        }
        if (notificationSent == null) {
            notificationSent = false;
        }
    }

    private String generateHolidayCode() {
        return "HLD" + System.currentTimeMillis() % 100000;
    }
}

enum HolidayType {
    NATIONAL, STATE, RELIGIOUS, COMPANY, CUSTOM
}
