package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.AttendanceResponse;
import com.hr.demo.hr.service.AttendanceService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;
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

    @PostMapping("/api/attendance/check-in")
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkIn(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String locationAddress,
            @RequestParam(value = "faceImage", required = false) MultipartFile faceImage,
            @RequestParam String checkInTime,
            @RequestParam String date) {
        var response = attendanceService.checkIn(currentUserId(), currentCompanyId(),
                latitude, longitude, locationAddress, faceImage, checkInTime, date);
        return ResponseEntity.ok(new ApiResponse<>(true, "Check-in successful", response));
    }

    @PostMapping("/api/attendance/check-out")
    public ResponseEntity<ApiResponse<AttendanceResponse>> checkOut(
            @RequestParam String checkOutTime,
            @RequestParam String date,
            @RequestParam(value = "faceImage", required = false) MultipartFile faceImage) {
        var response = attendanceService.checkOut(currentUserId(), currentCompanyId(), checkOutTime, date, faceImage);
        return ResponseEntity.ok(new ApiResponse<>(true, "Check-out successful", response));
    }

    @GetMapping("/api/attendance/my")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> myAttendance() {
        var records = attendanceService.getMyAttendance(currentUserId());
        return ResponseEntity.ok(new ApiResponse<>(true, "My attendance fetched", records));
    }

    @GetMapping("/api/attendance/today")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> todayAttendance() {
        var records = attendanceService.getTodayAttendance(currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "Today's attendance fetched", records));
    }

    @GetMapping("/api/attendance")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> allAttendance() {
        var records = attendanceService.getCompanyAttendance(currentCompanyId());
        return ResponseEntity.ok(new ApiResponse<>(true, "All attendance fetched", records));
    }
}
