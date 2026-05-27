package com.hr.demo.service;

import com.hr.demo.dto.CreateDepartmentRequest;
import com.hr.demo.dto.UpdateDepartmentRequest;
import com.hr.demo.reaponse.DepartmentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(CreateDepartmentRequest request, Long companyId);

    DepartmentResponse updateDepartment(Long departmentId, UpdateDepartmentRequest request, Long companyId);

    void deleteDepartment(Long departmentId, Long companyId);

    DepartmentResponse getDepartment(Long departmentId, Long companyId);

    List<DepartmentResponse> getCompanyDepartments(Long companyId);

    Page<DepartmentResponse> listDepartments(Long companyId, int page, int size, String search, String sortBy,
            String sortDirection);

    DepartmentResponse setStatus(Long departmentId, boolean active, Long companyId);
}
