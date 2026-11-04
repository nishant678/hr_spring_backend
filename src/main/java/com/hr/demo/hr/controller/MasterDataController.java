package com.hr.demo.hr.controller;

import com.hr.demo.exceptions.UnauthorizedException;
import com.hr.demo.reaponse.ApiResponse;
import com.hr.demo.reaponse.MasterDataResponse;
import com.hr.demo.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MasterDataController {
    private final SecurityUtil securityUtil;

    @GetMapping("/api/master-data")
    public ApiResponse<MasterDataResponse> masterData() {
        var user = securityUtil.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Unauthenticated"));
        return new ApiResponse<>(true, "Master data fetched", MasterDataResponse.forUser(user));
    }
}
