package com.hr.demo.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.AssetResponse;
import com.hr.demo.service.AssetService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final SecurityUtil securityUtil;

    private UserEntity getCurrentUser() {
        return securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
    }

    private Long currentCompanyId() {
        UserEntity user = getCurrentUser();
        if (user.getCompany() == null) {
            throw new UnauthorizedException("User not linked to a company");
        }
        return user.getCompany().getId();
    }

    // Employee: my assigned assets
    @GetMapping("/api/assets/my")
    public ResponseEntity<ApiResponse<List<AssetResponse>>> getMyAssets() {
        UserEntity user = getCurrentUser();
        List<AssetResponse> list = assetService.getMyAssets(user.getId());
        return ResponseEntity.ok(new ApiResponse<>(true, "My assets fetched", list));
    }

    // Admin: list all company assets
    @GetMapping("/api/assets")
    public ResponseEntity<ApiResponse<List<AssetResponse>>> getAssets() {
        List<AssetResponse> list = assetService.getAssets(currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Assets fetched", list));
    }

    // Admin: get single asset
    @GetMapping("/api/assets/{id}")
    public ResponseEntity<ApiResponse<AssetResponse>> getAsset(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Asset fetched", assetService.getAsset(id)));
    }

    // Admin: create asset
    @PostMapping("/api/assets")
    public ResponseEntity<ApiResponse<AssetResponse>> createAsset(@RequestBody Map<String, String> body) {
        AssetResponse response = assetService.createAsset(
                currentCompanyId(),
                body.get("name"),
                body.get("assetTag"),
                body.get("serialNumber"),
                body.get("model"),
                body.get("brand"),
                body.get("type"),
                body.get("purchaseDate"),
                body.get("warrantyExpiry"),
                body.get("notes"));
        return ResponseEntity.ok(new ApiResponse<>(true, "Asset created", response));
    }

    // Admin: update asset
    @PutMapping("/api/assets/{id}")
    public ResponseEntity<ApiResponse<AssetResponse>> updateAsset(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        AssetResponse response = assetService.updateAsset(
                id,
                body.get("name"),
                body.get("assetTag"),
                body.get("serialNumber"),
                body.get("model"),
                body.get("brand"),
                body.get("type"),
                body.get("purchaseDate"),
                body.get("warrantyExpiry"),
                body.get("status"),
                body.get("notes"));
        return ResponseEntity.ok(new ApiResponse<>(true, "Asset updated", response));
    }

    // Admin: delete asset
    @DeleteMapping("/api/assets/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Asset deleted", null));
    }

    // Admin: assign asset to user
    @PostMapping("/api/assets/{id}/assign")
    public ResponseEntity<ApiResponse<AssetResponse>> assignAsset(
            @PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "userId is required", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Asset assigned",
                assetService.assignAsset(id, userId)));
    }

    // Admin: unassign asset
    @PostMapping("/api/assets/{id}/unassign")
    public ResponseEntity<ApiResponse<AssetResponse>> unassignAsset(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Asset unassigned",
                assetService.unassignAsset(id)));
    }

    // Admin: get assets assigned to a specific user
    @GetMapping("/api/assets/user/{userId}")
    public ResponseEntity<ApiResponse<List<AssetResponse>>> getUserAssets(@PathVariable Long userId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "User assets fetched",
                assetService.getUserAssets(userId)));
    }
}
