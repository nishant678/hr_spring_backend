package com.hr.demo.service.impl;

import com.hr.demo.entity.CompanyEntity;
import com.hr.demo.entity.ShiftEntity;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.repository.CompanyRepository;
import com.hr.demo.repository.ShiftRepository;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ShiftEntity> getShifts(Long companyId) {
        return shiftRepository.findByCompany_IdOrderByNameAsc(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftEntity getShift(Long id) {
        return shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found"));
    }

    @Override
    public long getEmployeeCount(Long companyId, String shiftName) {
        return userRepository.countByCompany_IdAndShift(companyId, shiftName);
    }

    @Override
    public ShiftEntity createShift(Long companyId, String name, String startTime, String endTime, String days, String description) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (name == null || name.isBlank()) throw new BadRequestException("Shift name is required");
        if (startTime == null || startTime.isBlank()) throw new BadRequestException("Start time is required");
        if (endTime == null || endTime.isBlank()) throw new BadRequestException("End time is required");
        if (days == null || days.isBlank()) throw new BadRequestException("Days are required");

        ShiftEntity shift = ShiftEntity.builder()
                .name(name.trim())
                .startTime(startTime.trim())
                .endTime(endTime.trim())
                .days(days.trim())
                .description(description)
                .company(company)
                .build();

        return shiftRepository.save(shift);
    }

    @Override
    public ShiftEntity updateShift(Long id, String name, String startTime, String endTime, String days, String description) {
        ShiftEntity shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found"));

        if (name != null && !name.isBlank()) shift.setName(name.trim());
        if (startTime != null && !startTime.isBlank()) shift.setStartTime(startTime.trim());
        if (endTime != null && !endTime.isBlank()) shift.setEndTime(endTime.trim());
        if (days != null && !days.isBlank()) shift.setDays(days.trim());
        if (description != null) shift.setDescription(description);

        return shiftRepository.save(shift);
    }

    @Override
    public void deleteShift(Long id) {
        ShiftEntity shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found"));
        shiftRepository.delete(shift);
    }
}
