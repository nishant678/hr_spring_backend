package com.hr.demo.hr.service;

import com.hr.demo.reaponse.AttendanceResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttendanceService {

    AttendanceResponse checkIn(Long userId, Long companyId, Double latitude, Double longitude,
                               String locationAddress, MultipartFile faceImage,
                               String checkInTime, String date);

    AttendanceResponse checkOut(Long userId, Long companyId, String checkOutTime, String date,
                                MultipartFile faceImage);

    List<AttendanceResponse> getMyAttendance(Long userId);

    List<AttendanceResponse> getTodayAttendance(Long companyId);

    List<AttendanceResponse> getCompanyAttendance(Long companyId);
}
