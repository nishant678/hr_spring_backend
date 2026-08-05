package com.hr.demo.service.impl;

import com.hr.demo.domain.attendance.AttendanceStatus;
import com.hr.demo.entity.AttendanceEntity;
import com.hr.demo.entity.CompanyEntity;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.reaponse.AttendanceResponse;
import com.hr.demo.repository.AttendanceRepository;
import com.hr.demo.repository.CompanyRepository;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.service.AttendanceService;
import com.hr.demo.service.FileStorageService;
import com.hr.demo.service.faceverify.FaceVerificationClient;
import com.hr.demo.service.faceverify.FaceVerificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final FileStorageService fileStorageService;
    private final FaceVerificationClient faceVerificationClient;

    @Override
    public AttendanceResponse checkIn(Long userId, Long companyId, Double latitude, Double longitude,
                                      String locationAddress, MultipartFile faceImage,
                                      String checkInTime, String date) {
        validateNotAlreadyCheckedIn(userId, date);

        UserEntity user = findUser(userId);
        CompanyEntity company = findCompany(companyId);

        LocalDate today = LocalDate.parse(date);
        LocalTime now = LocalTime.parse(checkInTime, DateTimeFormatter.ofPattern("HH:mm:ss"));

        String faceImageUrl = null;
        if (faceImage != null && !faceImage.isEmpty()) {
            if (faceVerificationClient.isEnabled()) {
                String employeeId = (user.getEmployeeId() != null && !user.getEmployeeId().isBlank())
                        ? user.getEmployeeId()
                        : String.valueOf(user.getId());
                FaceVerificationClient.VerifyOutcome outcome;
                try {
                    outcome = faceVerificationClient.verify(employeeId, faceImage);
                } catch (FaceVerificationException ex) {
                    throw new BadRequestException("Face verification unavailable: " + ex.getMessage());
                }
                if (!outcome.matched()) {
                    throw new BadRequestException(
                            "Face verification failed — chehra match nahi hua / Face does not match the registered employee "
                                    + "(score " + outcome.score() + ")");
                }
            }
            faceImageUrl = fileStorageService.storeFile(faceImage, companyId, userId);
        }

        AttendanceStatus status = determineStatus(now);
        double hours = 0.0;

        AttendanceEntity attendance = AttendanceEntity.builder()
                .date(today)
                .checkInTime(now)
                .status(status)
                .faceImageUrl(faceImageUrl)
                .hoursWorked(hours)
                .latitude(latitude)
                .longitude(longitude)
                .locationAddress(locationAddress)
                .user(user)
                .company(company)
                .build();

        attendanceRepository.save(attendance);
        return new AttendanceResponse(attendance);
    }

    @Override
    public AttendanceResponse checkOut(Long userId, Long companyId, String checkOutTime, String date,
                                       MultipartFile faceImage) {
        LocalDate today = LocalDate.parse(date);
        AttendanceEntity attendance = attendanceRepository.findByUser_IdAndDate(userId, today)
                .orElseThrow(() -> new BadRequestException("No check-in found for today"));

        if (attendance.getCheckOutTime() != null) {
            throw new BadRequestException("Already checked out today");
        }

        if (faceImage != null && !faceImage.isEmpty()) {
            if (faceVerificationClient.isEnabled()) {
                UserEntity user = findUser(userId);
                String employeeId = (user.getEmployeeId() != null && !user.getEmployeeId().isBlank())
                        ? user.getEmployeeId()
                        : String.valueOf(user.getId());
                FaceVerificationClient.VerifyOutcome outcome;
                try {
                    outcome = faceVerificationClient.verify(employeeId, faceImage);
                } catch (FaceVerificationException ex) {
                    throw new BadRequestException("Face verification unavailable: " + ex.getMessage());
                }
                if (!outcome.matched()) {
                    throw new BadRequestException(
                            "Face verification failed — chehra match nahi hua / Face does not match the registered employee "
                                    + "(score " + outcome.score() + ")");
                }
            }
        }

        LocalTime now = LocalTime.parse(checkOutTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        attendance.setCheckOutTime(now);

        double hours = ChronoUnit.MINUTES.between(attendance.getCheckInTime(), now) / 60.0;
        attendance.setHoursWorked(Math.round(hours * 100.0) / 100.0);

        attendanceRepository.save(attendance);
        return new AttendanceResponse(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getMyAttendance(Long userId) {
        findUser(userId);
        return attendanceRepository.findByUser_IdOrderByDateDescCheckInTimeDesc(userId)
                .stream()
                .map(AttendanceResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getTodayAttendance(Long companyId) {
        return attendanceRepository.findByCompany_IdAndDate(companyId, LocalDate.now())
                .stream()
                .map(AttendanceResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getCompanyAttendance(Long companyId) {
        return attendanceRepository.findByCompany_IdOrderByDateDescCheckInTimeDesc(companyId)
                .stream()
                .map(AttendanceResponse::new)
                .toList();
    }

    private void validateNotAlreadyCheckedIn(Long userId, String date) {
        LocalDate today = LocalDate.parse(date);
        attendanceRepository.findByUser_IdAndDate(userId, today)
                .ifPresent(a -> {
                    if (a.getCheckOutTime() == null) {
                        throw new BadRequestException("Already checked in today. Please check out first.");
                    }
                });
    }

    private AttendanceStatus determineStatus(LocalTime checkInTime) {
        LocalTime lateThreshold = LocalTime.of(10, 0);
        if (checkInTime.isAfter(LocalTime.of(14, 0))) {
            return AttendanceStatus.HALF_DAY;
        }
        if (checkInTime.isAfter(lateThreshold)) {
            return AttendanceStatus.LATE;
        }
        return AttendanceStatus.PRESENT;
    }

    private UserEntity findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private CompanyEntity findCompany(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }
}
