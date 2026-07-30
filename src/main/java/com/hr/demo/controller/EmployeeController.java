package com.hr.demo.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.UserResponse;
import com.hr.demo.service.UserService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class EmployeeController {

    private final SecurityUtil securityUtil;
    private final UserService userService;

    @GetMapping("/api/employee/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        UserEntity user = securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
        UserResponse response = userService.getUserProfile(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched", response));
    }
}
