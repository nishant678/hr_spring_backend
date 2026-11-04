package com.hr.demo.hr.service.faceverify;

import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.hr.repository.*;
import com.hr.demo.hr.service.FileStorageService;
import com.hr.demo.hr.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileEnrollmentTest {
    @Test void invalidSelfieNeverChangesProfilePhotoOrSavesAFile() {
        UserRepository users = mock(UserRepository.class);
        FileStorageService files = mock(FileStorageService.class);
        FaceRecognitionService faces = mock(FaceRecognitionService.class);
        var service = new UserServiceImpl(users, mock(PasswordEncoder.class), mock(DepartmentRepository.class),
                mock(DesignationRepository.class), mock(RoleRepository.class), mock(AssetRepository.class), files, faces);
        var user = UserEntity.builder().id(42L).employeeId("EMP42").firstName("Test").profilePhoto("old.jpg")
                .company(com.hr.demo.hr.entity.CompanyEntity.builder().id(7L).faceVerificationAttendance(true).build()).build();
        var image = new MockMultipartFile("image", "image.jpg", "image/jpeg", new byte[]{1});
        when(users.findById(42L)).thenReturn(Optional.of(user));
        when(faces.register(eq("EMP42"),any(),eq(image))).thenReturn(FaceRecognitionService.FaceRegistrationResult.failed("MASK_DETECTED", "Remove mask"));
        assertThatThrownBy(() -> service.updateProfilePhoto(42L,image)).hasMessageContaining("MASK_DETECTED");
        assertThat(user.getProfilePhoto()).isEqualTo("old.jpg");
        verifyNoInteractions(files);
        verify(users, never()).save(any());
    }
}
