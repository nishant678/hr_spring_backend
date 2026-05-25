package com.hr.demo.service;

import com.hr.demo.dto.CreateCompanyRequest;
import com.hr.demo.reaponse.CompanyResponse;

import java.util.List;

public interface SuperAdminService {

    CompanyResponse createCompany(CreateCompanyRequest request);

    CompanyResponse updateCompany(Long companyId, CreateCompanyRequest request);

    List<CompanyResponse> getAllCompanies();

    CompanyResponse deactivateCompany(Long companyId);

    CompanyResponse activateCompany(Long companyId);
}