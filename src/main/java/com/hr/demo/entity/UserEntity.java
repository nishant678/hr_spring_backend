package com.hr.demo.entity;
import com.hr.demo.domain.user.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // Personal Information
    private String firstName;
    private String middleName;
    private String lastName;
    private String employeeId;
    private LocalDate dateOfBirth;
    private String gender;
    private String maritalStatus;
    private String nationality;
    private String bloodGroup;
    private String panNumber;
    private String aadhaarNumber;
    private String passportNumber;
    private String profilePhoto;

    // Job Information
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designation_id")
    private DesignationEntity designation;

    private String employmentType;
    private LocalDate dateOfJoining;
    private String reportingManager;
    private String workLocation;
    private String probationPeriod;
    private LocalDate confirmationDate;
    private String shift;
    private String employeeCategory;
    private String costCenter;
    private String businessUnit;

    // Contact Information
    private String officialEmail;
    private String officialPhone;
    private String personalEmail;
    private String personalPhone;
    @Column(columnDefinition = "TEXT")
    private String currentAddress;
    @Column(columnDefinition = "TEXT")
    private String permanentAddress;

    // Salary & Payroll
    private String paySchedule;
    private String currency;
    private BigDecimal basicSalary;
    private BigDecimal grossSalary;
    private String paymentFrequency;
    private String bankName;
    private String bankAccountNumber;
    private String ifscCode;
    private String pfNumber;
    private String esiNumber;
    private String uanNumber;
    private String taxRegime;

    // Additional Information
    private String education;
    private Integer experienceYears;
    private String skills;
    private String languagesKnown;
    private String emergencyContactName;
    private String emergencyContactRelationship;
    private String emergencyContactPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_role_id")
    private RoleEntity userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    private String phone;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public String getUsername() { return email; }

    @Override
    public String getPassword() { return password; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
