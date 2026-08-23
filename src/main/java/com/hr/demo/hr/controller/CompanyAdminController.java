package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.hr.dto.AddCompanyUserRequest;
import com.hr.demo.hr.dto.UpdateCompanyUserRequest;
import com.hr.demo.hr.entity.CompanyEntity;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.CompanyResponse;
import com.hr.demo.reaponse.UserResponse;
import com.hr.demo.hr.repository.CompanyRepository;
import com.hr.demo.hr.repository.UserRepository;
import com.hr.demo.hr.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/company-admin")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class CompanyAdminController {

    private final UserService userService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> addUser(@Valid @RequestBody AddCompanyUserRequest request) {
        var response = userService.addCompanyUser(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully", response));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> listUsers() {
        var response = userService.getCompanyUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched", response));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        var response = userService.getCompanyUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User fetched", response));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyUserRequest request) {
        var response = userService.updateCompanyUser(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully", response));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteCompanyUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "User deleted", null));
    }

    @GetMapping("/company")
    public ResponseEntity<ApiResponse<CompanyResponse>> getCompany() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CompanyEntity company = user.getCompany();
        if (company == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "No company assigned", null));
        }
        CompanyResponse response = CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .ownerName(company.getOwnerName())
                .email(company.getEmail())
                .phone(company.getPhone())
                .website(company.getWebsite())
                .logoUrl(company.getLogoUrl())
                .address(company.getAddress())
                .city(company.getCity())
                .state(company.getState())
                .country(company.getCountry())
                .postalCode(company.getPostalCode())
                .gstNumber(company.getGstNumber())
                .panNumber(company.getPanNumber())
                .subscriptionPlan(company.getSubscriptionPlan())
                .employeeLimit(company.getEmployeeLimit())
                .subscriptionStart(company.getSubscriptionStart())
                .subscriptionEnd(company.getSubscriptionEnd())
                .timezone(company.getTimezone())
                .currency(company.getCurrency())
                .attendanceMandatory(company.getAttendanceMandatory())
                .autoEmailReports(company.getAutoEmailReports())
                .status(company.getStatus())
                .industryType(company.getIndustryType())
                .build();
        return ResponseEntity.ok(new ApiResponse<>(true, "Company fetched", response));
    }
}
