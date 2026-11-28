package com.hr.demo.hr.dto;

import java.util.List;

public record SidebarItemDto(
    String id,
    String label,
    String icon,
    String route,
    boolean permission,
    List<SidebarItemDto> children
) {
    public static SidebarItemDto leaf(String id, String label, String icon, String route) {
        return new SidebarItemDto(id, label, icon, route, true, null);
    }

    public static SidebarItemDto leaf(String id, String label, String icon, String route, boolean permission) {
        return new SidebarItemDto(id, label, icon, route, permission, null);
    }

    public static SidebarItemDto child(String id, String label, String route) {
        return new SidebarItemDto(id, label, null, route, true, null);
    }

    public static SidebarItemDto child(String id, String label, String route, boolean permission) {
        return new SidebarItemDto(id, label, null, route, permission, null);
    }

    public static SidebarItemDto group(String id, String label, String icon, List<SidebarItemDto> children) {
        return new SidebarItemDto(id, label, icon, null, true, children);
    }

    public static SidebarItemDto group(String id, String label, String icon, boolean permission, List<SidebarItemDto> children) {
        return new SidebarItemDto(id, label, icon, null, permission, children);
    }
}
