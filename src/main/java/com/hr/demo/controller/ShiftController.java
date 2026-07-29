package com.hr.demo.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.entity.ShiftEntity;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.ShiftResponse;
import com.hr.demo.service.ShiftService;
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
public class ShiftController {

    private final ShiftService shiftService;
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

    @GetMapping("/api/shifts")
    public ResponseEntity<ApiResponse<List<ShiftResponse>>> getShifts() {
        Long companyId = currentCompanyId();
        List<ShiftResponse> list = shiftService.getShifts(companyId)
                .stream()
                .map(s -> new ShiftResponse(s, shiftService.getEmployeeCount(companyId, s.getName())))
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(true, "Shifts fetched", list));
    }

    @GetMapping("/api/shifts/{id}")
    public ResponseEntity<ApiResponse<ShiftResponse>> getShift(@PathVariable Long id) {
        ShiftEntity entity = shiftService.getShift(id);
        Long companyId = currentCompanyId();
        ShiftResponse response = new ShiftResponse(entity, shiftService.getEmployeeCount(companyId, entity.getName()));
        return ResponseEntity.ok(new ApiResponse<>(true, "Shift fetched", response));
    }

    @PostMapping("/api/shifts")
    public ResponseEntity<ApiResponse<ShiftResponse>> createShift(@RequestBody Map<String, String> body) {
        Long companyId = currentCompanyId();
        ShiftEntity entity = shiftService.createShift(
                companyId,
                body.get("name"),
                body.get("startTime"),
                body.get("endTime"),
                body.get("days"),
                body.get("description"));
        return ResponseEntity.ok(new ApiResponse<>(true, "Shift created",
                new ShiftResponse(entity, shiftService.getEmployeeCount(companyId, entity.getName()))));
    }

    @PutMapping("/api/shifts/{id}")
    public ResponseEntity<ApiResponse<ShiftResponse>> updateShift(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        Long companyId = currentCompanyId();
        ShiftEntity entity = shiftService.updateShift(
                id,
                body.get("name"),
                body.get("startTime"),
                body.get("endTime"),
                body.get("days"),
                body.get("description"));
        return ResponseEntity.ok(new ApiResponse<>(true, "Shift updated",
                new ShiftResponse(entity, shiftService.getEmployeeCount(companyId, entity.getName()))));
    }

    @DeleteMapping("/api/shifts/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShift(@PathVariable Long id) {
        shiftService.deleteShift(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Shift deleted", null));
    }
}
