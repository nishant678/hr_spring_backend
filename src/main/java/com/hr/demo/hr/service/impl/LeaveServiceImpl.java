package com.hr.demo.hr.service.impl;

import com.hr.demo.domain.leave.LeaveStatus;
import com.hr.demo.hr.dto.CreateLeaveRequest;
import com.hr.demo.hr.dto.RejectLeaveRequest;
import com.hr.demo.hr.entity.CompanyEntity;
import com.hr.demo.hr.entity.LeaveEntity;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.reaponse.LeaveResponse;
import com.hr.demo.hr.repository.CompanyRepository;
import com.hr.demo.hr.repository.LeaveRepository;
import com.hr.demo.hr.repository.UserRepository;
import com.hr.demo.hr.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public LeaveResponse createLeave(CreateLeaveRequest request, Long userId, Long companyId) {
        if (request.getFromDate().isAfter(request.getToDate())) {
            throw new BadRequestException("From date cannot be after to date");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        LeaveEntity leave = LeaveEntity.builder()
                .leaveType(request.getLeaveType())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .fromTime(request.getFromTime())
                .toTime(request.getToTime())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .user(user)
                .company(company)
                .build();

        leaveRepository.save(leave);
        return new LeaveResponse(leave);
    }

    @Override
    public void uploadAttachment(Long leaveId, String attachmentUrl) {
        LeaveEntity leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));
        leave.setAttachmentUrl(attachmentUrl);
        leaveRepository.save(leave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveResponse> getMyLeaves(Long userId) {
        return leaveRepository.findByUser_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(LeaveResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaveResponse> getAllCompanyLeaves(Long companyId) {
        return leaveRepository.findByCompany_IdOrderByCreatedAtDesc(companyId)
                .stream()
                .map(LeaveResponse::new)
                .toList();
    }

    @Override
    public LeaveResponse approveLeave(Long leaveId, Long adminId) {
        LeaveEntity leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Leave is already " + leave.getStatus().name().toLowerCase());
        }

        leave.setStatus(LeaveStatus.APPROVED);
        leaveRepository.save(leave);
        return new LeaveResponse(leave);
    }

    @Override
    public LeaveResponse rejectLeave(Long leaveId, RejectLeaveRequest request, Long adminId) {
        LeaveEntity leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Leave is already " + leave.getStatus().name().toLowerCase());
        }

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setRejectionReason(request.getRejectionReason());
        leaveRepository.save(leave);
        return new LeaveResponse(leave);
    }

    @Override
    @Transactional(readOnly = true)
    public LeaveResponse getLeave(Long leaveId) {
        LeaveEntity leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave not found"));
        return new LeaveResponse(leave);
    }
}
