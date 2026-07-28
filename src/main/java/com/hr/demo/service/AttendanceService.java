package com.hr.demo.service;

import com.hr.demo.reaponse.AttendanceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttendanceService {

    AttendanceResponse checkIn(Long userId, Long companyId, Double latitude, Double longitude,
                               String locationAddress, MultipartFile faceImage);

    AttendanceResponse checkOut(Long userId, Long companyId);

    List<AttendanceResponse> getMyAttendance(Long userId);

    List<AttendanceResponse> getTodayAttendance(Long companyId);

    List<AttendanceResponse> getCompanyAttendance(Long companyId);
}
