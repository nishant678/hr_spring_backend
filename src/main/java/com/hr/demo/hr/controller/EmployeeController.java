package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.UserResponse;
import com.hr.demo.hr.service.FileStorageService;
import com.hr.demo.hr.service.UserService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class EmployeeController {

    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    @GetMapping("/api/employee/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        UserEntity user = securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
        UserResponse response = userService.getUserProfile(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched", response));
    }

    @PostMapping("/api/employee/profile/photo")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfilePhoto(
            @RequestParam("image") MultipartFile image) {
        UserEntity user = securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
        UserResponse response = userService.updateProfilePhoto(user.getId(), image);
        return ResponseEntity.ok(new ApiResponse<>(true, "Profile photo updated", response));
    }

    @GetMapping("/api/profile-photo/**")
    public ResponseEntity<Resource> serveProfilePhoto(HttpServletRequest request) {
        String path = request.getRequestURI()
                .substring(request.getRequestURI().indexOf("/api/profile-photo/") + "/api/profile-photo/".length());
        Resource resource = fileStorageService.loadFile(path);
        String filename = resource.getFilename() != null ? resource.getFilename() : path;
        MediaType contentType = contentTypeFor(filename);
        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + filename + "\"")
                .body(resource);
    }

    private MediaType contentTypeFor(String filename) {
        String name = filename.toLowerCase();
        if (name.endsWith(".png")) return MediaType.IMAGE_PNG;
        if (name.endsWith(".webp")) return MediaType.parseMediaType("image/webp");
        if (name.endsWith(".gif")) return MediaType.IMAGE_GIF;
        if (name.endsWith(".bmp")) return MediaType.parseMediaType("image/bmp");
        return MediaType.IMAGE_JPEG;
    }
}
