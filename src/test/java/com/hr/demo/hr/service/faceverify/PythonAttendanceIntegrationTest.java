package com.hr.demo.hr.service.faceverify;

import com.hr.demo.domain.company.IndustryType;
import com.hr.demo.domain.user.Role;
import com.hr.demo.hr.entity.*;
import com.hr.demo.hr.repository.*;
import com.hr.demo.hr.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.*;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Real Python + real PostgreSQL; all business records roll back after the test. */
@SpringBootTest(properties={"app.face-verify.base-url=http://127.0.0.1:8001", "spring.jpa.hibernate.ddl-auto=none"})
@Transactional
@EnabledIfEnvironmentVariable(named="FACE_TEST_DB",matches="true")
class PythonAttendanceIntegrationTest {
    @Autowired CompanyRepository companies;
    @Autowired UserRepository users;
    @Autowired UserService profiles;
    @Autowired AttendanceService attendance;
    @Autowired FaceRecognitionService faces;
    @MockBean FileStorageService files;

    @Test void profileEnrollmentAndBothAttendanceActionsUsePythonAndPostgres() throws Exception {
        String tag=UUID.randomUUID().toString().substring(0,8);
        var company=companies.saveAndFlush(CompanyEntity.builder().companyName("Flow Test " + tag)
                .ownerName("Test").email(tag+"@example.invalid").phone(tag).industryType(IndustryType.SOFTWARE).build());
        var user=users.saveAndFlush(UserEntity.builder().company(company).employeeId("TEST"+tag)
                .firstName("Flow").lastName("Test").email("user"+tag+"@example.invalid").password("test-only")
                .role(Role.EMPLOYEE).build());
        var path=Path.of(System.getenv("FACE_TEST_SELFIE"));
        var selfie=new MockMultipartFile("image","selfie.jpg","image/jpeg",Files.readAllBytes(path));
        when(files.storeProfilePhoto(any(),any())).thenReturn("test-only/selfie.jpg");
        when(files.storeFile(any(),any(),any())).thenReturn("test-only/attendance.jpg");
        assertThat(profiles.updateProfilePhoto(user.getId(),selfie).getProfilePhoto()).contains("test-only");
        assertThat(faces.isRegistered(user.getEmployeeId())).isTrue();
        // Updating the same profile twice must not produce FACE_ALREADY_REGISTERED.
        profiles.updateProfilePhoto(user.getId(),selfie);
        assertThat(attendance.checkIn(user.getId(),company.getId(),1.0,1.0,"Test",selfie,"09:00:00","2026-11-05").getCheckInTime()).hasToString("09:00");
        assertThat(attendance.checkOut(user.getId(),company.getId(),"18:00:00","2026-11-05",selfie).getHoursWorked()).isEqualTo(9.0);
        var masked=new MockMultipartFile("image","mask.png","image/png",Files.readAllBytes(Path.of("../face_verification_service/data/test-fixtures/mask.png")));
        assertThatThrownBy(() -> profiles.updateProfilePhoto(user.getId(),masked)).hasMessageContaining("MASK_DETECTED");
    }
}
