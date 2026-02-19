package com.hr.demo.service;

import com.hr.demo.dto.CreateRoleRequest;
import com.hr.demo.dto.UpdateRoleRequest;
import com.hr.demo.reaponse.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse createRole(CreateRoleRequest request);

    RoleResponse updateRole(Long roleId, UpdateRoleRequest request);

    void deleteRole(Long roleId);

    RoleResponse getRole(Long roleId);

    List<RoleResponse> getCompanyRoles(Long companyId);
}