package com.hr.demo.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.service.faceverify.FaceRecognitionService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
@Slf4j
public class FaceVerificationController {

    private final FaceRecognitionService faceRecognitionService;
    private final SecurityUtil securityUtil;

    private UserEntity currentUser() {
        return securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
    }

    private String employeeIdFor(UserEntity user) {
        return (user.getEmployeeId() != null && !user.getEmployeeId().isBlank())
                ? user.getEmployeeId() : String.valueOf(user.getId());
    }

    @PostMapping("/api/face/register")
    public ResponseEntity<ApiResponse<Map<String, Object>>> registerFace(
            @RequestParam("image") MultipartFile image) {
        UserEntity user = currentUser();
        String employeeId = employeeIdFor(user);
        String name = user.getFirstName()
                + (user.getLastName() != null && !user.getLastName().isBlank() ? " " + user.getLastName() : "");

        FaceRecognitionService.FaceRegistrationResult result =
                faceRecognitionService.register(employeeId, name, image);

        if (!result.success()) {
            throw new BadRequestException(result.message());
        }

        log.info("Face registered for employeeId={}", employeeId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Face registered successfully", Map.of(
                "employeeId", employeeId, "registered", true,
                "qualityScore", result.qualityScore(), "livenessScore", result.livenessScore())));
    }

    @GetMapping("/api/face/status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> faceStatus() {
        UserEntity user = currentUser();
        String employeeId = employeeIdFor(user);
        boolean registered = faceRecognitionService.isRegistered(employeeId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Face registration status",
                Map.of("employeeId", employeeId, "registered", registered)));
    }
}
