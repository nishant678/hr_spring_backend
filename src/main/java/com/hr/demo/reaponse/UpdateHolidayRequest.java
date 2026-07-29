package com.hr.demo.reaponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateHolidayRequest {
    private String name;
    private String date;
    private String type;
    private String description;
}
