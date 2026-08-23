package com.hr.demo.hr.service.impl;

import com.hr.demo.hr.dto.CreateRoleRequest;
import com.hr.demo.hr.dto.UpdateRoleRequest;
import com.hr.demo.hr.entity.CompanyEntity;
import com.hr.demo.hr.entity.RoleEntity;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.DuplicateResourceException;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.RoleResponse;
import com.hr.demo.hr.repository.CompanyRepository;
import com.hr.demo.hr.repository.RoleRepository;
import com.hr.demo.hr.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    private RoleResponse map(RoleEntity role) {
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.getCompany().getId());
    }

    // CREATE
    @Override
    public RoleResponse createRole(CreateRoleRequest request, Long companyId) {

        String name = request.getName() == null ? null : request.getName().trim();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Role name is required");
        }

        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (roleRepository.existsByNameIgnoreCaseAndCompanyId(name, companyId)) {
            throw new DuplicateResourceException("Role already exists in this company");
        }

        RoleEntity role = RoleEntity.builder()
                .name(name)
                .description(request.getDescription())
                .company(company)
                .build();

        return map(roleRepository.save(role));
    }

    // UPDATE
    @Override
    public RoleResponse updateRole(Long roleId, UpdateRoleRequest request, Long companyId) {

        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (!role.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Access denied");
        }

        String name = request.getName() == null ? null : request.getName().trim();
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Role name is required");
        }

        if (!role.getName().equalsIgnoreCase(name)
                && roleRepository.existsByNameIgnoreCaseAndCompanyId(name, companyId)) {
            throw new DuplicateResourceException("Role already exists in this company");
        }

        role.setName(name);
        role.setDescription(request.getDescription());

        return map(roleRepository.save(role));
    }

    // DELETE
    @Override
    public void deleteRole(Long roleId, Long companyId) {
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        if (!role.getCompany().getId().equals(companyId)) {
            throw new UnauthorizedException("Access denied");
        }

        roleRepository.deleteById(roleId);
    }

    // GET SINGLE
    @Override
    public RoleResponse getRole(Long roleId, Long companyId) {
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        if (!role.getCompany().getId().equals(companyId))
            throw new UnauthorizedException("Access denied");
        return map(role);
    }

    // GET COMPANY ROLES
    @Override
    public List<RoleResponse> getCompanyRoles(Long companyId) {
        return roleRepository.findAllByCompanyId(companyId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RoleResponse> listRoles(Long companyId, int page, int size, String search, String sortBy,
            String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection == null ? "ASC" : sortDirection),
                sortBy == null ? "name" : sortBy);
        var pageable = PageRequest.of(page, size, sort);
        var p = roleRepository.searchByCompany(companyId, search, pageable);
        return new PageImpl<>(p.getContent().stream().map(this::map).collect(Collectors.toList()), pageable,
                p.getTotalElements());
    }

    @Override
    public RoleResponse setStatus(Long roleId, boolean active, Long companyId) {
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        if (!role.getCompany().getId().equals(companyId))
            throw new UnauthorizedException("Access denied");
        role.setActive(active);
        return map(roleRepository.save(role));
    }
}
