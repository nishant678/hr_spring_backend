package com.hr.demo.service;

import com.hr.demo.dto.CreateRoleRequest;
import com.hr.demo.dto.UpdateRoleRequest;
import com.hr.demo.reaponse.RoleResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {

    RoleResponse createRole(CreateRoleRequest request, Long companyId);

    RoleResponse updateRole(Long roleId, UpdateRoleRequest request, Long companyId);

    void deleteRole(Long roleId, Long companyId);

    RoleResponse getRole(Long roleId, Long companyId);

    List<RoleResponse> getCompanyRoles(Long companyId);

    Page<RoleResponse> listRoles(Long companyId, int page, int size, String search, String sortBy,
            String sortDirection);

    RoleResponse setStatus(Long roleId, boolean active, Long companyId);
}