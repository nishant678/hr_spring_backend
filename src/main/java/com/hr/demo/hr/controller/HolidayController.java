package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.domain.holiday.HolidayType;
import com.hr.demo.hr.entity.HolidayEntity;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.CreateHolidayRequest;
import com.hr.demo.reaponse.HolidayResponse;
import com.hr.demo.reaponse.UpdateHolidayRequest;
import com.hr.demo.hr.service.HolidayService;
import com.hr.demo.util.SecurityUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;
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

    @GetMapping("/api/holidays")
    public ResponseEntity<ApiResponse<List<HolidayResponse>>> getHolidays() {
        List<HolidayResponse> list = holidayService.getHolidays(currentCompanyId())
                .stream()
                .map(HolidayResponse::new)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(true, "Holidays fetched", list));
    }

    @GetMapping("/api/holidays/{id}")
    public ResponseEntity<ApiResponse<HolidayResponse>> getHoliday(@PathVariable Long id) {
        HolidayResponse response = new HolidayResponse(holidayService.getHoliday(id));
        return ResponseEntity.ok(new ApiResponse<>(true, "Holiday fetched", response));
    }

    @PostMapping("/api/holidays")
    public ResponseEntity<ApiResponse<HolidayResponse>> createHoliday(
            @RequestBody CreateHolidayRequest req) {
        HolidayEntity entity = holidayService.createHoliday(
                currentCompanyId(), req.getName(), LocalDate.parse(req.getDate()),
                HolidayType.valueOf(req.getType().toUpperCase()), req.getDescription());
        return ResponseEntity.ok(new ApiResponse<>(true, "Holiday created", new HolidayResponse(entity)));
    }

    @PutMapping("/api/holidays/{id}")
    public ResponseEntity<ApiResponse<HolidayResponse>> updateHoliday(
            @PathVariable Long id,
            @RequestBody UpdateHolidayRequest req) {
        HolidayEntity entity = holidayService.updateHoliday(
                id, req.getName(),
                req.getDate() != null ? LocalDate.parse(req.getDate()) : null,
                req.getType() != null ? HolidayType.valueOf(req.getType().toUpperCase()) : null,
                req.getDescription());
        return ResponseEntity.ok(new ApiResponse<>(true, "Holiday updated", new HolidayResponse(entity)));
    }

    @DeleteMapping("/api/holidays/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHoliday(@PathVariable Long id) {
        holidayService.deleteHoliday(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Holiday deleted", null));
    }
}
