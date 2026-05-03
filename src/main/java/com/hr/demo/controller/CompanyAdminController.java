package com.hr.demo.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.dto.AddCompanyUserRequest;
import com.hr.demo.reaponse.UserResponse;
import com.hr.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/company-admin")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class CompanyAdminController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody AddCompanyUserRequest request) {
        return ResponseEntity.ok(userService.addCompanyUser(request));
    }
}
