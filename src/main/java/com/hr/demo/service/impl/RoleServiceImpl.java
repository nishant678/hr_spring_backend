package com.hr.demo.service.impl;

import com.hr.demo.dto.CreateRoleRequest;
import com.hr.demo.dto.UpdateRoleRequest;
import com.hr.demo.entity.CompanyEntity;
import com.hr.demo.entity.RoleEntity;
import com.hr.demo.reaponse.RoleResponse;
import com.hr.demo.repository.CompanyRepository;
import com.hr.demo.repository.RoleRepository;
import com.hr.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    private RoleResponse map(RoleEntity role){
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getCompany().getId()
        );
    }

    // CREATE
    @Override
    public RoleResponse createRole(CreateRoleRequest request) {

        CompanyEntity company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if(roleRepository.existsByNameIgnoreCaseAndCompanyId(request.getName(), request.getCompanyId()))
            throw new RuntimeException("Role already exists in this company");

        RoleEntity role = RoleEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .company(company)
                .build();

        return map(roleRepository.save(role));
    }

    // UPDATE
    @Override
    public RoleResponse updateRole(Long roleId, UpdateRoleRequest request) {

        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setName(request.getName());
        role.setDescription(request.getDescription());

        return map(roleRepository.save(role));
    }

    // DELETE
    @Override
    public void deleteRole(Long roleId) {
        if(!roleRepository.existsById(roleId))
            throw new RuntimeException("Role not found");

        roleRepository.deleteById(roleId);
    }

    // GET SINGLE
    @Override
    public RoleResponse getRole(Long roleId) {
        return roleRepository.findById(roleId)
                .map(this::map)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    // GET COMPANY ROLES
    @Override
    public List<RoleResponse> getCompanyRoles(Long companyId) {
        return roleRepository.findAllByCompanyId(companyId)
                .stream()
                .map(this::map)
                .toList();
    }
}