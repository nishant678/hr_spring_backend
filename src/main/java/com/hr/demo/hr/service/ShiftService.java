package com.hr.demo.hr.service;

import com.hr.demo.hr.entity.ShiftEntity;

import java.util.List;

public interface ShiftService {

    List<ShiftEntity> getShifts(Long companyId);

    ShiftEntity getShift(Long id);

    long getEmployeeCount(Long companyId, String shiftName);

    ShiftEntity createShift(Long companyId, String name, String startTime, String endTime, String days, String description);

    ShiftEntity updateShift(Long id, String name, String startTime, String endTime, String days, String description);

    void deleteShift(Long id);
}
