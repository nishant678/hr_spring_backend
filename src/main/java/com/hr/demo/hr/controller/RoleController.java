package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.hr.dto.CreateRoleRequest;
import com.hr.demo.hr.dto.UpdateRoleRequest;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.RoleResponse;
import com.hr.demo.hr.service.RoleService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;
    private final SecurityUtil securityUtil;

    private Long currentCompanyId() {
        Optional<UserEntity> currentUser = securityUtil.getCurrentUser();

        if (currentUser.isEmpty()) {
            log.error("currentCompanyId: No authenticated user found");
            throw new UnauthorizedException("Unauthenticated");
        }

        UserEntity user = currentUser.get();
        log.info("currentCompanyId: User {} authenticated", user.getEmail());

        if (user.getCompany() == null) {
            log.error("currentCompanyId: User {} not linked to any company", user.getEmail());
            throw new UnauthorizedException("User not linked to a company");
        }

        Long companyId = user.getCompany().getId();
        log.info("currentCompanyId: Returning company {} for user {}", companyId, user.getEmail());
        return companyId;
    }

    private void validateCompanyPath(Long companyId) {
        if (!companyId.equals(currentCompanyId())) {
            throw new UnauthorizedException("Access denied");
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody CreateRoleRequest request) {
        log.info("create: Creating role with name: {}", request.getName());
        Long currentCompanyId = currentCompanyId();
        var response = roleService.createRole(request, currentCompanyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Role created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> update(
            @PathVariable Long id,
            @RequestBody UpdateRoleRequest request) {
        log.info("update: Updating role {} with name: {}", id, request.getName());
        var response = roleService.updateRole(id, request, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Role updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        log.info("delete: Deleting role {}", id);
        roleService.deleteRole(id, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Role deleted", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> get(@PathVariable Long id) {
        log.info("get: Fetching role {}", id);
        var response = roleService.getRole(id, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Role fetched", response));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getCompanyRoles(@PathVariable Long companyId) {
        log.info("getCompanyRoles: Fetching roles for company {}", companyId);
        validateCompanyPath(companyId);
        var response = roleService.getCompanyRoles(companyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Roles fetched", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<RoleResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {
        log.info("list: Fetching roles with page: {}, size: {}, search: {}", page, size, search);
        var response = roleService.listRoles(currentCompanyId(), page, size, search, sortBy, sortDirection);
        return ResponseEntity.ok(new ApiResponse<>(true, "Roles listed", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RoleResponse>> setStatus(@PathVariable Long id,
            @RequestParam boolean active) {
        log.info("setStatus: Setting role {} status to {}", id, active);
        var response = roleService.setStatus(id, active, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Role status updated", response));
    }
}
