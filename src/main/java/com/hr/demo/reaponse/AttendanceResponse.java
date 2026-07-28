package com.hr.demo.reaponse;

import com.hr.demo.domain.attendance.AttendanceStatus;
import com.hr.demo.entity.AttendanceEntity;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class AttendanceResponse {

    private final Long id;
    private final LocalDate date;
    private final LocalTime checkInTime;
    private final LocalTime checkOutTime;
    private final AttendanceStatus status;
    private final String faceImageUrl;
    private final Double hoursWorked;
    private final Double latitude;
    private final Double longitude;
    private final String locationAddress;
    private final String notes;
    private final Long userId;
    private final String userEmail;
    private final String userName;
    private final Long companyId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AttendanceResponse(AttendanceEntity attendance) {
        this.id = attendance.getId();
        this.date = attendance.getDate();
        this.checkInTime = attendance.getCheckInTime();
        this.checkOutTime = attendance.getCheckOutTime();
        this.status = attendance.getStatus();
        this.faceImageUrl = attendance.getFaceImageUrl();
        this.hoursWorked = attendance.getHoursWorked();
        this.latitude = attendance.getLatitude();
        this.longitude = attendance.getLongitude();
        this.locationAddress = attendance.getLocationAddress();
        this.notes = attendance.getNotes();
        this.userId = attendance.getUser().getId();
        this.userEmail = attendance.getUser().getEmail();
        this.userName = (attendance.getUser().getFirstName() != null ? attendance.getUser().getFirstName() : "")
                + " " + (attendance.getUser().getLastName() != null ? attendance.getUser().getLastName() : "");
        this.companyId = attendance.getCompany().getId();
        this.createdAt = attendance.getCreatedAt();
        this.updatedAt = attendance.getUpdatedAt();
    }
}
