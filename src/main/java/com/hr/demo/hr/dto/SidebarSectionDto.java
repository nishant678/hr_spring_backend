package com.hr.demo.hr.dto;

import java.util.List;

public record SidebarSectionDto(
    String title,
    List<SidebarItemDto> items
) {}
