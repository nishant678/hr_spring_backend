package com.hr.demo.reaponse;

import com.hr.demo.domain.user.Role;
import com.hr.demo.entity.UserEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

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

        this.firstName = user.getFirstName();
        this.middleName = user.getMiddleName();
        this.lastName = user.getLastName();
        this.employeeId = user.getEmployeeId();
        this.dateOfBirth = user.getDateOfBirth();
        this.gender = user.getGender();
        this.maritalStatus = user.getMaritalStatus();
        this.nationality = user.getNationality();
        this.bloodGroup = user.getBloodGroup();
        this.panNumber = user.getPanNumber();
        this.aadhaarNumber = user.getAadhaarNumber();
        this.passportNumber = user.getPassportNumber();
        this.profilePhoto = user.getProfilePhoto();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.role = user.getRole().name();

        if (user.getDepartment() != null) {
            this.departmentId = user.getDepartment().getId();
            this.departmentName = user.getDepartment().getName();
        }
        if (user.getDesignation() != null) {
            this.designationId = user.getDesignation().getId();
            this.designationName = user.getDesignation().getName();
        }
        if (user.getUserRole() != null) {
            this.userRoleId = user.getUserRole().getId();
            this.userRoleName = user.getUserRole().getName();
        }

        this.employmentType = user.getEmploymentType();
        this.dateOfJoining = user.getDateOfJoining();
        this.reportingManager = user.getReportingManager();
        this.workLocation = user.getWorkLocation();
        this.probationPeriod = user.getProbationPeriod();
        this.confirmationDate = user.getConfirmationDate();
        this.shift = user.getShift();
        this.employeeCategory = user.getEmployeeCategory();
        this.costCenter = user.getCostCenter();
        this.businessUnit = user.getBusinessUnit();

        this.officialEmail = user.getOfficialEmail();
        this.officialPhone = user.getOfficialPhone();
        this.personalEmail = user.getPersonalEmail();
        this.personalPhone = user.getPersonalPhone();
        this.currentAddress = user.getCurrentAddress();
        this.permanentAddress = user.getPermanentAddress();

        this.paySchedule = user.getPaySchedule();
        this.currency = user.getCurrency();
        this.basicSalary = user.getBasicSalary();
        this.grossSalary = user.getGrossSalary();
        this.paymentFrequency = user.getPaymentFrequency();
        this.bankName = user.getBankName();
        this.bankAccountNumber = user.getBankAccountNumber();
        this.ifscCode = user.getIfscCode();
        this.pfNumber = user.getPfNumber();
        this.esiNumber = user.getEsiNumber();
        this.uanNumber = user.getUanNumber();
        this.taxRegime = user.getTaxRegime();

        this.education = user.getEducation();
        this.experienceYears = user.getExperienceYears();
        this.skills = user.getSkills();
        this.languagesKnown = user.getLanguagesKnown();
        this.emergencyContactName = user.getEmergencyContactName();
        this.emergencyContactRelationship = user.getEmergencyContactRelationship();
        this.emergencyContactPhone = user.getEmergencyContactPhone();
    }
}
