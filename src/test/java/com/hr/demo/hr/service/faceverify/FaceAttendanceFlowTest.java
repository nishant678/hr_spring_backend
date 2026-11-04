package com.hr.demo.hr.service.faceverify;

import com.hr.demo.domain.user.Role;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.hr.entity.*;
import com.hr.demo.hr.repository.*;
import com.hr.demo.hr.service.FileStorageService;
import com.hr.demo.hr.service.impl.AttendanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockMultipartFile;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FaceAttendanceFlowTest {
    final FaceVerificationClient python = mock(FaceVerificationClient.class);
    final FaceProfileRepository faces = mock(FaceProfileRepository.class);
    final UserRepository users = mock(UserRepository.class);
    final AttendanceRepository records = mock(AttendanceRepository.class);
    final CompanyRepository companies = mock(CompanyRepository.class);
    final FileStorageService files = mock(FileStorageService.class);
    final FaceRecognitionService recognition = new FaceRecognitionService(python, faces, users);
    final AttendanceServiceImpl attendance = new AttendanceServiceImpl(records, users, companies, files, recognition);
    final MockMultipartFile image = new MockMultipartFile("image", "selfie.jpg", "image/jpeg", new byte[]{1});
    final AtomicReference<FaceProfileEntity> enrollment = new AtomicReference<>();
    final AtomicReference<AttendanceEntity> record = new AtomicReference<>();
    final CompanyEntity company = CompanyEntity.builder().id(7L).build();
    final UserEntity user = UserEntity.builder().id(42L).employeeId("EMP42").role(Role.EMPLOYEE).company(company).build();

    @BeforeEach void setup() {
        when(faces.findActiveByEmployeeId("EMP42")).thenAnswer(i -> Optional.ofNullable(enrollment.get()));
        when(faces.save(any())).thenAnswer(i -> { FaceProfileEntity f = i.getArgument(0); f.setId(1L); enrollment.set(f); return f; });
        when(users.findByEmployeeId("EMP42")).thenReturn(Optional.of(user));
        when(users.findById(42L)).thenReturn(Optional.of(user));
        when(companies.findById(7L)).thenReturn(Optional.of(company));
        when(records.findByUser_IdAndDate(eq(42L), any(LocalDate.class))).thenAnswer(i -> Optional.ofNullable(record.get()));
        when(records.save(any())).thenAnswer(i -> { AttendanceEntity a = i.getArgument(0); a.setId(1L); record.set(a); return a; });
        when(files.storeFile(any(), any(), any())).thenReturn("selfie.jpg");
    }

    static FaceProcessResponse response(int axis, String failure) {
        return new FaceProcessResponse(failure == null, failure,
                IntStream.range(0, 512).mapToObj(i -> i == axis ? 1.0 : 0.0).toList(),
                1, .9, .9, true, 0, "buffalo_l", "v1", 512, failure == null ? "Valid" : failure, null);
    }

    void enroll() {
        when(python.processRegistration(image)).thenReturn(response(0, null));
        assertThat(recognition.register("EMP42", "Employee", image).success()).isTrue();
    }

    @Test void registrationThenCheckInAndCheckOutUseTheSameSavedFace() {
        enroll();
        when(python.processVerify(image)).thenReturn(response(0, null));
        assertThat(attendance.checkIn(42L, 7L, 1.0, 1.0, "Office", image, "09:00:00", "2026-11-05").getCheckInTime()).hasToString("09:00");
        assertThat(attendance.checkOut(42L, 7L, "18:00:00", "2026-11-05", image).getHoursWorked()).isEqualTo(9.0);
        verify(python, times(2)).processVerify(image);
        verify(python, never()).verify(any(), any());
    }

    @Test void profileSelfieCanReplaceAnExistingEnrollment() {
        enroll();
        Long id = enrollment.get().getId();
        when(python.processRegistration(image)).thenReturn(response(1, null));
        assertThat(recognition.register("EMP42", "Employee", image).success()).isTrue();
        assertThat(enrollment.get().getId()).isEqualTo(id);
        assertThat(enrollment.get().getEmbedding()[1]).isEqualTo(1);
    }

    @ParameterizedTest @ValueSource(strings={"FACE_BLURRY", "MASK_DETECTED", "MULTIPLE_FACES", "NO_FACE"})
    void rejectedReplacementPreservesTheOldFace(String reason) {
        enroll();
        when(python.processRegistration(image)).thenReturn(response(1, reason));
        assertThat(recognition.register("EMP42", "Employee", image).reason()).isEqualTo(reason);
        assertThat(enrollment.get().getEmbedding()[0]).isEqualTo(1);
    }

    @Test void anotherPersonCannotCheckInOrCheckOut() {
        enroll();
        when(python.processVerify(image)).thenReturn(response(1, null));
        assertThatThrownBy(() -> attendance.checkIn(42L,7L,null,null,null,image,"09:00:00","2026-11-05"))
                .isInstanceOf(BadRequestException.class).hasMessageContaining("FACE_NOT_MATCHED");
        assertThat(record.get()).isNull();
        when(python.processVerify(image)).thenReturn(response(0, null));
        attendance.checkIn(42L,7L,null,null,null,image,"09:00:00","2026-11-05");
        when(python.processVerify(image)).thenReturn(response(1, null));
        assertThatThrownBy(() -> attendance.checkOut(42L,7L,"18:00:00","2026-11-05",image)).hasMessageContaining("FACE_NOT_MATCHED");
        assertThat(record.get().getCheckOutTime()).isNull();
    }

    @Test void missingSelfieCannotBypassEitherAttendanceAction() {
        enroll();
        assertThatThrownBy(() -> attendance.checkIn(42L,7L,null,null,null,null,"09:00:00","2026-11-05"))
                .hasMessageContaining("FACE_IMAGE_REQUIRED");
        when(python.processVerify(image)).thenReturn(response(0, null));
        attendance.checkIn(42L,7L,null,null,null,image,"09:00:00","2026-11-05");
        assertThatThrownBy(() -> attendance.checkOut(42L,7L,"18:00:00","2026-11-05",null)).hasMessageContaining("FACE_IMAGE_REQUIRED");
    }

    @ParameterizedTest @ValueSource(strings={"FACE_BLURRY", "MASK_DETECTED", "MULTIPLE_FACES", "LIVENESS_FAILED"})
    void qualityFailureBlocksBothAttendanceActions(String reason) {
        enroll();
        when(python.processVerify(image)).thenReturn(response(0, reason));
        assertThatThrownBy(() -> attendance.checkIn(42L,7L,null,null,null,image,"09:00:00","2026-11-05"))
                .hasMessageContaining(reason);
        when(python.processVerify(image)).thenReturn(response(0, null));
        attendance.checkIn(42L,7L,null,null,null,image,"09:00:00","2026-11-05");
        when(python.processVerify(image)).thenReturn(response(0, reason));
        assertThatThrownBy(() -> attendance.checkOut(42L,7L,"18:00:00","2026-11-05",image)).hasMessageContaining(reason);
    }

    @Test void attendanceNeverEnrollsAnUnregisteredFace() {
        assertThat(recognition.verify("EMP42",image).reason()).isEqualTo("NO_REGISTERED_FACE");
        verifyNoInteractions(python);
    }

    @Test void pythonUnavailableBlocksAttendance() {
        enroll();
        when(python.processVerify(image)).thenThrow(new FaceVerificationException("offline"));
        assertThatThrownBy(() -> attendance.checkIn(42L,7L,null,null,null,image,"09:00:00","2026-11-05"))
                .hasMessageContaining("SERVICE_UNAVAILABLE");
        verify(records, never()).save(any());
    }
}
