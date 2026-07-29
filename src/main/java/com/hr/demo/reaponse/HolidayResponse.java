package com.hr.demo.reaponse;

import com.hr.demo.domain.holiday.HolidayType;
import com.hr.demo.entity.HolidayEntity;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class HolidayResponse {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final HolidayType type;
    private final String description;

    public HolidayResponse(HolidayEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.date = entity.getDate();
        this.type = entity.getType();
        this.description = entity.getDescription();
    }
}
