package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.reaponse.CompanyResponse;
import com.hr.demo.hr.dto.CreateCompanyRequest;
import com.hr.demo.hr.service.SuperAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @PostMapping("/create-companies")
    public CompanyResponse createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        return superAdminService.createCompany(request);
    }

    @PutMapping("/companies/{id}")
    public CompanyResponse updateCompany(@PathVariable Long id, @Valid @RequestBody CreateCompanyRequest request) {
        return superAdminService.updateCompany(id, request);
    }

    @GetMapping("/companies")
    public List<CompanyResponse> getCompanies() {
        return superAdminService.getAllCompanies();
    }

    @PutMapping("/companies/{id}/deactivate")
    public CompanyResponse deactivate(@PathVariable Long id) {
        return superAdminService.deactivateCompany(id);
    }

    @PutMapping("/companies/{id}/activate")
    public CompanyResponse activate(@PathVariable Long id) {
        return superAdminService.activateCompany(id);
    }
}
