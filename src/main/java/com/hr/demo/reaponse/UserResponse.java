package com.hr.demo.reaponse;

import com.hr.demo.domain.user.Role;
import com.hr.demo.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public class UserResponse {
    private Long id;
    private Long companyId;

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
    private String email;
    private String phone;

    // Auth / Role
    private String role;
    private Long userRoleId;
    private String userRoleName;

    // Department / Designation
    private Long departmentId;
    private String departmentName;
    private Long designationId;
    private String designationName;

    // Job Information
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
    private String currentAddress;
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

    // Assets
    @Setter
    private List<AssignedAssetResponse> assets;

    // Additional Information
    private String education;
    private Integer experienceYears;
    private String skills;
    private String languagesKnown;
    private String emergencyContactName;
    private String emergencyContactRelationship;
    private String emergencyContactPhone;

    public UserResponse(UserEntity user) {
        this.id = user.getId();
        if (user.getCompany() != null) {
            this.companyId = user.getCompany().getId();
        }

        this.firstName = nvl(user.getFirstName());
        this.middleName = nvl(user.getMiddleName());
        this.lastName = nvl(user.getLastName());
        this.employeeId = nvl(user.getEmployeeId());
        this.dateOfBirth = user.getDateOfBirth();
        this.gender = nvl(user.getGender());
        this.maritalStatus = nvl(user.getMaritalStatus());
        this.nationality = nvl(user.getNationality());
        this.bloodGroup = nvl(user.getBloodGroup());
        this.panNumber = nvl(user.getPanNumber());
        this.aadhaarNumber = nvl(user.getAadhaarNumber());
        this.passportNumber = nvl(user.getPassportNumber());
        this.profilePhoto = nvl(user.getProfilePhoto());
        this.email = nvl(user.getEmail());
        this.phone = nvl(user.getPhone());
        this.role = user.getRole().name();

        if (user.getDepartment() != null) {
            this.departmentId = user.getDepartment().getId();
            this.departmentName = nvl(user.getDepartment().getName());
        }
        if (user.getDesignation() != null) {
            this.designationId = user.getDesignation().getId();
            this.designationName = nvl(user.getDesignation().getName());
        }
        if (user.getUserRole() != null) {
            this.userRoleId = user.getUserRole().getId();
            this.userRoleName = nvl(user.getUserRole().getName());
        }

        this.employmentType = nvl(user.getEmploymentType());
        this.dateOfJoining = user.getDateOfJoining();
        this.reportingManager = nvl(user.getReportingManager());
        this.workLocation = nvl(user.getWorkLocation());
        this.probationPeriod = nvl(user.getProbationPeriod());
        this.confirmationDate = user.getConfirmationDate();
        this.shift = nvl(user.getShift());
        this.employeeCategory = nvl(user.getEmployeeCategory());
        this.costCenter = nvl(user.getCostCenter());
        this.businessUnit = nvl(user.getBusinessUnit());

        this.officialEmail = nvl(user.getOfficialEmail());
        this.officialPhone = nvl(user.getOfficialPhone());
        this.personalEmail = nvl(user.getPersonalEmail());
        this.personalPhone = nvl(user.getPersonalPhone());
        this.currentAddress = nvl(user.getCurrentAddress());
        this.permanentAddress = nvl(user.getPermanentAddress());

        this.paySchedule = nvl(user.getPaySchedule());
        this.currency = nvl(user.getCurrency());
        this.basicSalary = user.getBasicSalary();
        this.grossSalary = user.getGrossSalary();
        this.paymentFrequency = nvl(user.getPaymentFrequency());
        this.bankName = nvl(user.getBankName());
        this.bankAccountNumber = nvl(user.getBankAccountNumber());
        this.ifscCode = nvl(user.getIfscCode());
        this.pfNumber = nvl(user.getPfNumber());
        this.esiNumber = nvl(user.getEsiNumber());
        this.uanNumber = nvl(user.getUanNumber());
        this.taxRegime = nvl(user.getTaxRegime());

        this.education = nvl(user.getEducation());
        this.experienceYears = user.getExperienceYears();
        this.skills = nvl(user.getSkills());
        this.languagesKnown = nvl(user.getLanguagesKnown());
        this.emergencyContactName = nvl(user.getEmergencyContactName());
        this.emergencyContactRelationship = nvl(user.getEmergencyContactRelationship());
        this.emergencyContactPhone = nvl(user.getEmergencyContactPhone());
    }

    private static String nvl(String value) {
        return value == null ? "" : value;
    }
}
