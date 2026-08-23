package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.hr.dto.CreateDesignationRequest;
import com.hr.demo.hr.dto.UpdateDesignationRequest;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.DesignationResponse;
import com.hr.demo.hr.service.DesignationService;
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
@RequestMapping("/api/designations")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class DesignationController {

    private final DesignationService designationService;
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
    public ResponseEntity<ApiResponse<DesignationResponse>> create(
            @Valid @RequestBody CreateDesignationRequest request) {
        var response = designationService.createDesignation(request, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Designation created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DesignationResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDesignationRequest request) {
        var response = designationService.updateDesignation(id, request, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Designation updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        designationService.deleteDesignation(id, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Designation deleted", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DesignationResponse>> get(@PathVariable Long id) {
        var response = designationService.getDesignation(id, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Designation fetched", response));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<ApiResponse<List<DesignationResponse>>> getCompanyDesignations(@PathVariable Long companyId) {
        validateCompanyPath(companyId);
        var response = designationService.getCompanyDesignations(companyId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Designations fetched", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DesignationResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {
        var response = designationService.listDesignations(currentCompanyId(), page, size, search, sortBy,
                sortDirection);
        return ResponseEntity.ok(new ApiResponse<>(true, "Designations listed", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<DesignationResponse>> setStatus(@PathVariable Long id,
            @RequestParam boolean active) {
        var response = designationService.setStatus(id, active, currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Designation status updated", response));
    }
}
