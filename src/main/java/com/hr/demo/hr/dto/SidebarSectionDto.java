package com.hr.demo.hr.dto;

import java.util.List;

public record SidebarSectionDto(
    String title,
    boolean permission,
    List<SidebarItemDto> items
) {
    public SidebarSectionDto(String title, List<SidebarItemDto> items) {
        this(title, true, items);
    }
}
