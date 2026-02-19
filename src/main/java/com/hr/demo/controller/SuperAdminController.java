package com.hr.demo.controller;

import com.hr.demo.reaponse.CompanyResponse;
import com.hr.demo.dto.CreateCompanyRequest;
import com.hr.demo.service.SuperAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @PostMapping("/companies")
    public CompanyResponse createCompany(@Valid @RequestBody CreateCompanyRequest request) {
        return superAdminService.createCompany(request);
    }

    @GetMapping("/companies")
    public List<CompanyResponse> getCompanies() {
        return superAdminService.getAllCompanies();
    }

    @PutMapping("/companies/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        superAdminService.deactivateCompany(id);
    }
}
