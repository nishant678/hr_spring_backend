package com.hr.demo.hr.service;

import com.hr.demo.hr.dto.CreateLeaveRequest;
import com.hr.demo.hr.dto.RejectLeaveRequest;
import com.hr.demo.reaponse.LeaveResponse;

import java.util.List;

public interface LeaveService {

    LeaveResponse createLeave(CreateLeaveRequest request, Long userId, Long companyId);

    void uploadAttachment(Long leaveId, String attachmentUrl);

    List<LeaveResponse> getMyLeaves(Long userId);

    List<LeaveResponse> getAllCompanyLeaves(Long companyId);

    LeaveResponse approveLeave(Long leaveId, Long adminId);

    LeaveResponse rejectLeave(Long leaveId, RejectLeaveRequest request, Long adminId);

    LeaveResponse getLeave(Long leaveId);
}
