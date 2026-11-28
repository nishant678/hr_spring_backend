package com.hr.demo.hr.service;

import com.hr.demo.hr.dto.CreateCompanyRequest;
import com.hr.demo.hr.dto.UpgradeCompanyPlanRequest;
import com.hr.demo.reaponse.CompanyResponse;

import java.util.List;

public interface SuperAdminService {

    CompanyResponse createCompany(CreateCompanyRequest request);

    CompanyResponse updateCompany(Long companyId, CreateCompanyRequest request);

    CompanyResponse upgradeCompanyPlan(Long companyId, UpgradeCompanyPlanRequest request);

    List<CompanyResponse> getAllCompanies();

    CompanyResponse deactivateCompany(Long companyId);

    CompanyResponse activateCompany(Long companyId);
    CompanyResponse updatePermissions(Long companyId, boolean faceVerificationAttendance, Boolean task);
}
