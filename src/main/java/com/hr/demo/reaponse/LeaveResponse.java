package com.hr.demo.reaponse;

import com.hr.demo.domain.leave.LeaveStatus;
import com.hr.demo.domain.leave.LeaveType;
import com.hr.demo.hr.entity.LeaveEntity;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class LeaveResponse {

    private final Long id;
    private final LeaveType leaveType;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final LocalTime fromTime;
    private final LocalTime toTime;
    private final String reason;
    private final String attachmentUrl;
    private final LeaveStatus status;
    private final String rejectionReason;
    private final Long userId;
    private final String userEmail;
    private final String userName;
    private final Long companyId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public LeaveResponse(LeaveEntity leave) {
        this.id = leave.getId();
        this.leaveType = leave.getLeaveType();
        this.fromDate = leave.getFromDate();
        this.toDate = leave.getToDate();
        this.fromTime = leave.getFromTime();
        this.toTime = leave.getToTime();
        this.reason = leave.getReason();
        this.attachmentUrl = leave.getAttachmentUrl();
        this.status = leave.getStatus();
        this.rejectionReason = leave.getRejectionReason();
        this.userId = leave.getUser().getId();
        this.userEmail = leave.getUser().getEmail();
        this.userName = (leave.getUser().getFirstName() != null ? leave.getUser().getFirstName() : "")
                + " " + (leave.getUser().getLastName() != null ? leave.getUser().getLastName() : "");
        this.companyId = leave.getCompany().getId();
        this.createdAt = leave.getCreatedAt();
        this.updatedAt = leave.getUpdatedAt();
    }
}
