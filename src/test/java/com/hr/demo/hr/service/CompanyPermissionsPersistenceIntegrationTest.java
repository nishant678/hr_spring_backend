package com.hr.demo.hr.service;

import com.hr.demo.domain.company.IndustryType;
import com.hr.demo.domain.user.Role;
import com.hr.demo.hr.entity.*;
import com.hr.demo.hr.repository.*;
import com.hr.demo.hr.service.faceverify.FaceRecognitionService;
import com.hr.demo.reaponse.MasterDataResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@EnabledIfEnvironmentVariable(named="FACE_TEST_DB",matches="true")
class CompanyPermissionsPersistenceIntegrationTest {
    @Autowired CompanyRepository companies;
    @Autowired UserRepository users;
    @Autowired SuperAdminService superAdmin;
    @Autowired AttendanceService attendance;
    @Autowired EntityManager em;
    @MockBean FaceRecognitionService faces;
    @MockBean FileStorageService files;

    @Test void companyPermissionPersistsAndControlsBothAttendanceActions() {
        String tag=UUID.randomUUID().toString().substring(0,8);
        var company=companies.saveAndFlush(CompanyEntity.builder().companyName("Permission Test " + tag)
                .ownerName("Test").email(tag+"@example.invalid").phone(tag).industryType(IndustryType.SOFTWARE).build());
        var user=users.saveAndFlush(UserEntity.builder().company(company).employeeId("TEST"+tag)
                .firstName("Permission").email("user"+tag+"@example.invalid").password("test-only")
                .role(Role.EMPLOYEE).build());
        Long companyId=company.getId(), userId=user.getId();
        superAdmin.updatePermissions(companyId,false,true);
        em.flush(); em.clear();
        assertThat(MasterDataResponse.forUser(users.findById(userId).orElseThrow()).attendance())
                .containsEntry("face_attendense",false);
        assertThat(MasterDataResponse.forUser(users.findById(userId).orElseThrow()).permissions())
                .containsEntry("task",true);
        assertThat(attendance.checkIn(userId,companyId,null,null,null,null,"09:00:00","2026-11-05").getFaceImageUrl()).isNull();
        assertThat(attendance.checkOut(userId,companyId,"18:00:00","2026-11-05",null).getHoursWorked()).isEqualTo(9.0);
        superAdmin.updatePermissions(companyId,true,false);
        em.flush(); em.clear();
        assertThat(MasterDataResponse.forUser(users.findById(userId).orElseThrow()).attendance())
                .containsEntry("face_attendense",true);
        assertThat(MasterDataResponse.forUser(users.findById(userId).orElseThrow()).permissions())
                .containsEntry("task",false);
        assertThatThrownBy(() -> attendance.checkIn(userId,companyId,null,null,null,null,"09:00:00","2026-11-06"))
                .hasMessageContaining("FACE_IMAGE_REQUIRED");
        verifyNoInteractions(faces,files);
    }
}
