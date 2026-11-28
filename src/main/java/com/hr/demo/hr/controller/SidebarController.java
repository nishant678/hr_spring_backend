package com.hr.demo.hr.controller;

import com.hr.demo.config.OpenApiConfig;
import com.hr.demo.domain.user.Role;
import com.hr.demo.hr.dto.SidebarSectionDto;
import com.hr.demo.hr.service.SidebarService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sidebar")
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RequiredArgsConstructor
public class SidebarController {

    private final SidebarService sidebarService;

    @GetMapping
    public ResponseEntity<List<SidebarSectionDto>> getSidebarForCurrentUser(Authentication authentication) {
        Role role = extractRole(authentication);
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePrivate())
            .body(sidebarService.getSidebarForRole(role));
    }

    private Role extractRole(Authentication authentication) {
        if (authentication == null) {
            return Role.DISABLED;
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String raw = authority.getAuthority();
            if (raw != null && raw.startsWith("ROLE_")) {
                return Role.from(raw.substring(5));
            }
        }
        return Role.EMPLOYEE;
    }
}
