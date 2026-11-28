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

    @PutMapping("/companies/{id}/permissions")
    public CompanyResponse updatePermissions(@PathVariable Long id,
            @Valid @RequestBody com.hr.demo.hr.dto.CompanyPermissionsRequest request) {
        return superAdminService.updatePermissions(id, request.faceVerificationAttendance(), request.task());
    }

    @GetMapping("/app-settings")
    public java.util.Map<String, Object> appSettings() {
        return java.util.Map.of("companies", superAdminService.getAllCompanies(), "permissions", List.of(
                java.util.Map.of("key", "face_attendense", "responseField", "faceVerificationAttendance",
                        "label", "Face Attendance", "description", "Require selfie verification for check-in and check-out."),
                java.util.Map.of("key", "task", "responseField", "taskEnabled",
                        "label", "Task", "description", "Show the Task feature for this company's users.")));
    }

    @PutMapping("/companies/{id}/deactivate")
    public CompanyResponse deactivate(@PathVariable Long id) {
        return superAdminService.deactivateCompany(id);
    }

    @PutMapping("/companies/{id}/activate")
    public CompanyResponse activate(@PathVariable Long id) {
        return superAdminService.activateCompany(id);
    }

    @PostMapping(value = "/companies/upload-logo", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public java.util.Map<String, String> uploadCompanyLogo(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "logo.png";
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')) : ".png";
        java.nio.file.Path dir = java.nio.file.Paths.get("uploads", "company-logos").toAbsolutePath().normalize();
        java.nio.file.Files.createDirectories(dir);
        String filename = java.util.UUID.randomUUID() + ext;
        file.transferTo(dir.resolve(filename).toFile());
        return java.util.Map.of("logoUrl", "/api/super-admin/company-logo/" + filename);
    }

    @GetMapping("/company-logo/{filename:.+}")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> getCompanyLogo(
            @PathVariable String filename) throws java.io.IOException {
        java.nio.file.Path filePath = java.nio.file.Paths.get("uploads", "company-logos")
                .toAbsolutePath().normalize().resolve(filename).normalize();
        if (!java.nio.file.Files.exists(filePath)) {
            return org.springframework.http.ResponseEntity.notFound().build();
        }
        String contentType = java.nio.file.Files.probeContentType(filePath);
        org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(filePath.toUri());
        return org.springframework.http.ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType != null ? contentType : "image/png"))
                .body(resource);
    }
}
