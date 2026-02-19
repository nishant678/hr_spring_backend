package com.hr.demo.service;
import com.hr.demo.dto.CreateCompanyRequest;
import com.hr.demo.reaponse.CompanyResponse;

import java.util.List;

public interface SuperAdminService {

    CompanyResponse createCompany(CreateCompanyRequest request);

    List<CompanyResponse> getAllCompanies();

    void deactivateCompany(Long companyId);
}