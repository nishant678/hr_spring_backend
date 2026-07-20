package com.hr.demo.dto;

import com.hr.demo.domain.leave.LeaveType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class CreateLeaveRequest {

    @NotNull
    private LeaveType leaveType;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    private LocalTime fromTime;

    private LocalTime toTime;

    private String reason;
}
