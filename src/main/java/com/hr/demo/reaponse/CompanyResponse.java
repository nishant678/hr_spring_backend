package com.hr.demo.reaponse;
import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.domain.user.SubscriptionPlan;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CompanyResponse {
    private Long id;
    private String companyName;
    private String ownerName;
    private String email;
    private String phone;
    private String website;
    private String logoUrl;

    // ADDRESS
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    // LEGAL
    private String gstNumber;
    private String panNumber;

    // SUBSCRIPTION
    private SubscriptionPlan subscriptionPlan;
    private Integer employeeLimit;
    private LocalDate subscriptionStart;
    private LocalDate subscriptionEnd;

    private String timezone;
    private String currency;
    private Boolean attendanceMandatory;
    private Boolean autoEmailReports;

    private CompanyStatus status;
}