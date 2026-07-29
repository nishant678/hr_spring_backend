package com.hr.demo.reaponse;

import com.hr.demo.entity.ShiftEntity;
import lombok.Getter;

@Getter
public class ShiftResponse {

    private final Long id;
    private final String name;
    private final String startTime;
    private final String endTime;
    private final String days;
    private final String description;
    private final long employeeCount;

    public ShiftResponse(ShiftEntity entity, long employeeCount) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
        this.days = entity.getDays();
        this.description = entity.getDescription();
        this.employeeCount = employeeCount;
    }
}
