package com.hr.demo.hr.dto;

import java.util.List;

public record SidebarItemDto(
    String id,
    String label,
    String icon,
    String route,
    List<SidebarItemDto> children
) {
    public static SidebarItemDto leaf(String id, String label, String icon, String route) {
        return new SidebarItemDto(id, label, icon, route, null);
    }

    public static SidebarItemDto child(String id, String label, String route) {
        return new SidebarItemDto(id, label, null, route, null);
    }

    public static SidebarItemDto group(String id, String label, String icon, List<SidebarItemDto> children) {
        return new SidebarItemDto(id, label, icon, null, children);
    }
}
