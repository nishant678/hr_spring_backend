package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.hr.dto.CreateLeaveRequest;
import com.hr.demo.hr.dto.RejectLeaveRequest;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.LeaveResponse;
import com.hr.demo.hr.service.FileStorageService;
import com.hr.demo.hr.service.LeaveService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final FileStorageService fileStorageService;
    private final SecurityUtil securityUtil;

    private UserEntity getCurrentUser() {
        return securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
    }

    private Long currentUserId() {
        return getCurrentUser().getId();
    }

    private Long currentCompanyId() {
        UserEntity user = getCurrentUser();
        if (user.getCompany() == null) {
            throw new UnauthorizedException("User not linked to a company");
        }
        return user.getCompany().getId();
    }

    @PostMapping("/api/leaves")
    public ResponseEntity<ApiResponse<LeaveResponse>> createLeave(
            @Valid @RequestBody CreateLeaveRequest request) {
        var response = leaveService.createLeave(request, currentUserId(), currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Leave applied successfully", response));
    }

    @PostMapping("/api/leaves/{id}/attachment")
    public ResponseEntity<ApiResponse<LeaveResponse>> uploadAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.storeFile(file, currentCompanyId(), id);
        leaveService.uploadAttachment(id, filePath);
        var leave = leaveService.getLeave(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Attachment uploaded", leave));
    }

    @GetMapping("/api/leaves/my")
    public ResponseEntity<ApiResponse<List<LeaveResponse>>> myLeaves() {
        var leaves = leaveService.getMyLeaves(currentUserId());
        return ResponseEntity.ok(new ApiResponse<>(true, "My leaves fetched", leaves));
    }

    @GetMapping("/api/leaves")
    public ResponseEntity<ApiResponse<List<LeaveResponse>>> allLeaves() {
        var leaves = leaveService.getAllCompanyLeaves(currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "All leaves fetched", leaves));
    }

    @GetMapping("/api/leaves/{id}")
    public ResponseEntity<ApiResponse<LeaveResponse>> getLeave(@PathVariable Long id) {
        var leave = leaveService.getLeave(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Leave fetched", leave));
    }

    @PutMapping("/api/leaves/{id}/approve")
    public ResponseEntity<ApiResponse<LeaveResponse>> approveLeave(@PathVariable Long id) {
        var leave = leaveService.approveLeave(id, currentUserId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Leave approved", leave));
    }

    @PutMapping("/api/leaves/{id}/reject")
    public ResponseEntity<ApiResponse<LeaveResponse>> rejectLeave(
            @PathVariable Long id,
            @Valid @RequestBody RejectLeaveRequest request) {
        var leave = leaveService.rejectLeave(id, request, currentUserId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Leave rejected", leave));
    }

    @GetMapping("/api/files/{companyId}/{filename}")
    public ResponseEntity<Resource> serveFile(
            @PathVariable Long companyId,
            @PathVariable String filename) {
        Resource resource = fileStorageService.loadFile("leaves/" + companyId + "/" + filename);
        String contentType = "application/octet-stream";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
