package com.hr.demo.hr.service.impl;

import com.hr.demo.domain.holiday.HolidayType;
import com.hr.demo.hr.entity.CompanyEntity;
import com.hr.demo.hr.entity.HolidayEntity;
import com.hr.demo.exceptions.BadRequestException;
import com.hr.demo.exceptions.ResourceNotFoundException;
import com.hr.demo.hr.repository.CompanyRepository;
import com.hr.demo.hr.repository.HolidayRepository;
import com.hr.demo.hr.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;
    private final CompanyRepository companyRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HolidayEntity> getHolidays(Long companyId) {
        return holidayRepository.findByCompany_IdOrderByDateDesc(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public HolidayEntity getHoliday(Long id) {
        return holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));
    }

    @Override
    public HolidayEntity createHoliday(Long companyId, String name, LocalDate date, HolidayType type, String description) {
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (name == null || name.isBlank()) {
            throw new BadRequestException("Holiday name is required");
        }
        if (date == null) {
            throw new BadRequestException("Holiday date is required");
        }
        if (type == null) {
            throw new BadRequestException("Holiday type is required");
        }

        HolidayEntity holiday = HolidayEntity.builder()
                .name(name.trim())
                .date(date)
                .type(type)
                .description(description)
                .company(company)
                .build();

        return holidayRepository.save(holiday);
    }

    @Override
    public HolidayEntity updateHoliday(Long id, String name, LocalDate date, HolidayType type, String description) {
        HolidayEntity holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));

        if (name != null && !name.isBlank()) {
            holiday.setName(name.trim());
        }
        if (date != null) {
            holiday.setDate(date);
        }
        if (type != null) {
            holiday.setType(type);
        }
        holiday.setDescription(description);

        return holidayRepository.save(holiday);
    }

    @Override
    public void deleteHoliday(Long id) {
        HolidayEntity holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));
        holidayRepository.delete(holiday);
    }
}
