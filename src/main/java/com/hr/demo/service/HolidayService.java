package com.hr.demo.service;

import com.hr.demo.domain.holiday.HolidayType;
import com.hr.demo.entity.HolidayEntity;

import java.time.LocalDate;
import java.util.List;

public interface HolidayService {

    List<HolidayEntity> getHolidays(Long companyId);

    HolidayEntity getHoliday(Long id);

    HolidayEntity createHoliday(Long companyId, String name, LocalDate date, HolidayType type, String description);

    HolidayEntity updateHoliday(Long id, String name, LocalDate date, HolidayType type, String description);

    void deleteHoliday(Long id);
}
