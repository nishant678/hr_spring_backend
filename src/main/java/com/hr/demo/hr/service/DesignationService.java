package com.hr.demo.hr.service;

import com.hr.demo.hr.dto.CreateDesignationRequest;
import com.hr.demo.hr.dto.UpdateDesignationRequest;
import com.hr.demo.reaponse.DesignationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DesignationService {

    DesignationResponse createDesignation(CreateDesignationRequest request, Long companyId);

    DesignationResponse updateDesignation(Long designationId, UpdateDesignationRequest request, Long companyId);

    void deleteDesignation(Long designationId, Long companyId);

    DesignationResponse getDesignation(Long designationId, Long companyId);

    List<DesignationResponse> getCompanyDesignations(Long companyId);

    Page<DesignationResponse> listDesignations(Long companyId, int page, int size, String search, String sortBy,
            String sortDirection);

    DesignationResponse setStatus(Long designationId, boolean active, Long companyId);
}
