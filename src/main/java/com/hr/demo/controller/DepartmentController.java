package com.hr.demo.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.dto.CreateDepartmentRequest;
import com.hr.demo.dto.UpdateDepartmentRequest;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.DepartmentResponse;
import com.hr.demo.service.DepartmentService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final SecurityUtil securityUtil;

    private Long currentCompanyId() {
        Optional<UserEntity> currentUser = securityUtil.getCurrentUser();
        return currentUser.map(user -> {
            if (user.getCompany() == null) {
                throw new UnauthorizedException("User not linked to a company");
            }
            return user.getCompany().getId();
        }).orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
    }

    private void validateCompanyPath(Long companyId) {
        if (!companyId.equals(currentCompanyId())) {
            throw new UnauthorizedException("Access denied");
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> create(@Valid @RequestBody CreateDepartmentRequest request) {
        var response = departmentService.createDepartment(request, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Department created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentRequest request) {
        var response = departmentService.updateDepartment(id, request, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Department updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Department deleted", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> get(@PathVariable Long id) {
        var response = departmentService.getDepartment(id, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Department fetched", response));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getCompanyDepartments(@PathVariable Long companyId) {
        validateCompanyPath(companyId);
        var response = departmentService.getCompanyDepartments(companyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Departments fetched", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DepartmentResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {
        var response = departmentService.listDepartments(currentCompanyId(), page, size, search, sortBy, sortDirection);
        return ResponseEntity.ok(new ApiResponse<>(true, "Departments listed", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<DepartmentResponse>> setStatus(@PathVariable Long id,
            @RequestParam boolean active) {
        var response = departmentService.setStatus(id, active, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Department status updated", response));
    }
}
