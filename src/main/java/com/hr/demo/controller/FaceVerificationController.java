package com.hr.demo.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.service.faceverify.FaceVerificationClient;
import com.hr.demo.service.faceverify.FaceVerificationException;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class FaceVerificationController {

    private final FaceVerificationClient faceVerificationClient;
    private final SecurityUtil securityUtil;

    private UserEntity currentUser() {
        return securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
    }

    private String employeeIdFor(UserEntity user) {
        return (user.getEmployeeId() != null && !user.getEmployeeId().isBlank())
                ? user.getEmployeeId()
                : String.valueOf(user.getId());
    }

    @PostMapping("/api/face/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> registerFace(
            @RequestParam("image") MultipartFile image) {
        UserEntity user = currentUser();
        String employeeId = employeeIdFor(user);
        String name = user.getFirstName()
                + (user.getLastName() != null && !user.getLastName().isBlank() ? " " + user.getLastName() : "");
        faceVerificationClient.register(employeeId, name, image);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Face registered successfully",
                Map.of("employeeId", employeeId, "registered", true)));
    }

    @GetMapping("/api/face/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> faceStatus() {
        UserEntity user = currentUser();
        String employeeId = employeeIdFor(user);
        boolean registered;
        try {
            registered = faceVerificationClient.isRegistered(employeeId);
        } catch (FaceVerificationException ex) {
            registered = false;
        }
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Face registration status",
                Map.of("employeeId", employeeId, "registered", registered)));
    }
}
