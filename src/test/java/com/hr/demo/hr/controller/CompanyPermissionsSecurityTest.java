package com.hr.demo.hr.controller;

import com.hr.demo.config.SecurityConfig;
import com.hr.demo.domain.user.Role;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.hr.service.SuperAdminService;
import com.hr.demo.reaponse.CompanyResponse;
import com.hr.demo.security.JwtAuthenticationFilter;
import com.hr.demo.security.JwtService;
import com.hr.demo.util.SecurityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({SuperAdminController.class, MasterDataController.class})
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class CompanyPermissionsSecurityTest {
    @Autowired MockMvc mvc;
    @MockBean JwtService jwt;
    @MockBean UserDetailsService details;
    @MockBean SuperAdminService service;
    @MockBean SecurityUtil security;

    void actor(Role role) {
        var user=UserEntity.builder().id(42L).email("test@example.invalid").role(role).build();
        when(jwt.extractUsername("test-token")).thenReturn(user.getEmail());
        when(jwt.isTokenValid(eq("test-token"),any())).thenReturn(true);
        when(details.loadUserByUsername(user.getEmail())).thenReturn(user);
        when(security.getCurrentUser()).thenReturn(Optional.of(user));
    }

    @Test void unauthenticatedPermissionUpdateIsRejected() throws Exception {
        mvc.perform(put("/api/super-admin/companies/7/permissions").contentType("application/json")
                .content("{\"faceVerificationAttendance\":false}")).andExpect(status().isUnauthorized());
        verifyNoInteractions(service);
    }

    @Test void companyAdminCannotChangePermissions() throws Exception {
        actor(Role.COMPANY_ADMIN);
        mvc.perform(put("/api/super-admin/companies/7/permissions").header("Authorization","Bearer test-token")
                .contentType("application/json").content("{\"faceVerificationAttendance\":false}"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(service);
    }

    @Test void masterAdminCanChangePermissions() throws Exception {
        actor(Role.MASTER_ADMIN);
        when(service.updatePermissions(7L,false,null)).thenReturn(CompanyResponse.builder().id(7L).faceVerificationAttendance(false).build());
        mvc.perform(put("/api/super-admin/companies/7/permissions").header("Authorization","Bearer test-token")
                .contentType("application/json").content("{\"faceVerificationAttendance\":false}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.faceVerificationAttendance").value(false));
    }

    @Test void missingPermissionIsNotInterpretedAsFalse() throws Exception {
        actor(Role.MASTER_ADMIN);
        mvc.perform(put("/api/super-admin/companies/7/permissions").header("Authorization","Bearer test-token")
                .contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test void authenticatedMasterDataDoesNotRequireACompanyQueryParameter() throws Exception {
        actor(Role.EMPLOYEE);
        mvc.perform(get("/api/master-data").header("Authorization","Bearer test-token"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.data.userId").value(42))
                .andExpect(jsonPath("$.data.attendance.enabled").value(false));
    }

    @Test void appSettingsDefinitionsAreDynamicAndSuperadminOnly() throws Exception {
        actor(Role.MASTER_ADMIN);
        when(service.getAllCompanies()).thenReturn(java.util.List.of());
        mvc.perform(get("/api/super-admin/app-settings").header("Authorization","Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.permissions[0].key").value("face_attendense"))
                .andExpect(jsonPath("$.permissions[1].key").value("task"));
    }

    @Test void canonicalSettingsPayloadSavesFaceAndTaskIndependently() throws Exception {
        actor(Role.MASTER_ADMIN);
        when(service.updatePermissions(7L,false,true)).thenReturn(CompanyResponse.builder().id(7L)
                .faceVerificationAttendance(false).taskEnabled(true).build());
        mvc.perform(put("/api/super-admin/companies/7/permissions").header("Authorization","Bearer test-token")
                .contentType("application/json").content("{\"face_attendense\":false,\"task\":true}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.taskEnabled").value(true))
                .andExpect(jsonPath("$.faceVerificationAttendance").value(false));
    }
}
