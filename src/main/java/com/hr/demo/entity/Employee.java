package com.hr.demo.entity;

import com.hr.demo.enums.EmployeeRole;
import com.hr.demo.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employees", indexes = {
    @Index(name = "idx_employee_company", columnList = "companyId"),
    @Index(name = "idx_employee_email", columnList = "email"),
    @Index(name = "idx_employee_phone", columnList = "phone"),
    @Index(name = "idx_employee_status", columnList = "status"),
    @Index(name = "idx_employee_department", columnList = "departmentId")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "employee_code", unique = true, nullable = false, length = 20)
    private String employeeCode;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(name = "alternative_phone")
    private String alternativePhone;

    @Column(nullable = false)
    private String password;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "employment_type", nullable = false)
    private String employmentType;

    @Column(name = "work_location")
    private String workLocation;

    @Column(name = "reporting_manager_id")
    private Long reportingManagerId;

    @Column(name = "shift_id")
    private Long shiftId;

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Column(name = "probation_end_date")
    private LocalDate probationEndDate;

    @Column(name = "confirmation_date")
    private LocalDate confirmationDate;

    @Column(name = "salary", nullable = false)
    private Double salary;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_number")
    private String bankAccountNumber;

    @Column(name = "bank_ifsc")
    private String bankIfsc;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "aadhaar_number")
    private String aadhaarNumber;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "gps_enabled", nullable = false)
    private Boolean gpsEnabled;

    @Column(name = "face_recognition_enabled", nullable = false)
    private Boolean faceRecognitionEnabled;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attendance> attendances;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Leave> leaves;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payroll> payrolls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

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
        if (employeeCode == null) {
            employeeCode = generateEmployeeCode();
        }
        if (status == null) {
            status = EmployeeStatus.ACTIVE;
        }
        if (isActive == null) {
            isActive = true;
        }
        if (role == null) {
            role = EmployeeRole.EMPLOYEE;
        }
        if (gpsEnabled == null) {
            gpsEnabled = true;
        }
        if (faceRecognitionEnabled == null) {
            faceRecognitionEnabled = false;
        }
    }

    private String generateEmployeeCode() {
        return "EMP" + System.currentTimeMillis() % 100000;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
