package com.hr.demo.hr.service;

import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.hr.entity.CompanyEntity;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.hr.repository.*;
import com.hr.demo.hr.service.impl.SuperAdminServiceImpl;
import com.hr.demo.reaponse.MasterDataResponse;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyPermissionsTest {
    @Test void permissionUpdateOnlyChangesTheSelectedCompanyPolicy() {
        var companies=mock(CompanyRepository.class);
        var company=CompanyEntity.builder().id(7L).companyName("Test Co").status(CompanyStatus.ACTIVE).build();
        when(companies.findById(7L)).thenReturn(Optional.of(company));
        var service=new SuperAdminServiceImpl(companies,mock(UserRepository.class),mock(DepartmentRepository.class),
                mock(DesignationRepository.class),mock(PasswordEncoder.class));
        assertThat(service.updatePermissions(7L,false,true).isFaceVerificationAttendance()).isFalse();
        assertThat(company.isTaskEnabled()).isTrue();
        assertThat(company.getCompanyName()).isEqualTo("Test Co");
        assertThat(company.getStatus()).isEqualTo(CompanyStatus.ACTIVE);
        assertThat(service.updatePermissions(7L,true,null).isFaceVerificationAttendance()).isTrue();
        assertThat(company.isTaskEnabled()).isTrue();
        verify(companies,times(2)).save(company);
    }

    @Test void masterDataReflectsTheAuthenticatedUsersCompany() {
        var company=CompanyEntity.builder().id(7L).status(CompanyStatus.ACTIVE).faceVerificationAttendance(false).build();
        var user=UserEntity.builder().id(42L).company(company).build();
        var data=MasterDataResponse.forUser(user);
        assertThat(data.companyId()).isEqualTo(7L);
        assertThat(data.userId()).isEqualTo(42L);
        assertThat(data.attendance()).containsEntry("face_attendense",false).containsEntry("enabled",true);
        assertThat(data.permissions()).containsEntry("task",false);
        company.setTaskEnabled(true);
        assertThat(MasterDataResponse.forUser(user).permissions()).containsEntry("task",true);
        company.setFaceVerificationAttendance(true);
        assertThat(MasterDataResponse.forUser(user).attendance()).containsEntry("face_attendense",true);
    }

    @Test void usersWithoutACompanyAndInactiveCompaniesCannotRecordAttendance() {
        var user=UserEntity.builder().id(42L).build();
        assertThat(MasterDataResponse.forUser(user).attendance()).containsEntry("enabled",false);
        user.setCompany(CompanyEntity.builder().id(7L).status(CompanyStatus.INACTIVE).build());
        assertThat(MasterDataResponse.forUser(user).attendance()).containsEntry("enabled",false);
    }

    @Test void newCompaniesDoNotReceiveFaceAttendanceByDefault() {
        assertThat(CompanyEntity.builder().build().isFaceVerificationAttendance()).isFalse();
    }
}
