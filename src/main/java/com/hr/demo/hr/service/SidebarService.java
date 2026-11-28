package com.hr.demo.hr.service;

import com.hr.demo.domain.user.Role;
import com.hr.demo.hr.dto.SidebarItemDto;
import com.hr.demo.hr.dto.SidebarSectionDto;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * O(1) time & O(1) per-request space role-indexed Sidebar provider.
 * Pre-computes immutable menu trees per Role at startup so requests never
 * scan or allocate unused role data.
 */
@Service
public class SidebarService {

    private final Map<Role, List<SidebarSectionDto>> roleMenuIndex = new EnumMap<>(Role.class);

    public SidebarService() {
        roleMenuIndex.put(Role.MASTER_ADMIN, buildMasterAdminMenu());
        roleMenuIndex.put(Role.COMPANY_ADMIN, buildCompanyAdminMenu());
        roleMenuIndex.put(Role.HR, buildCrmUserMenu());
        roleMenuIndex.put(Role.EMPLOYEE, buildCrmUserMenu());
        roleMenuIndex.put(Role.DISABLED, List.of());
    }

    public List<SidebarSectionDto> getSidebarForRole(Role role) {
        return roleMenuIndex.getOrDefault(role, List.of());
    }

    private List<SidebarSectionDto> buildMasterAdminMenu() {
        return List.of(
            new SidebarSectionDto("Main Menu", List.of(
                SidebarItemDto.group("sub-31", "Super Admin", "ti ti-user-star", List.of(
                    SidebarItemDto.child("nav-23", "Dashboard", "/super-admin-dashboard"),
                    SidebarItemDto.child("nav-24", "Companies", "/company"),
                    SidebarItemDto.child("nav-25", "Subscriptions", "/subscription"),
                    SidebarItemDto.child("nav-26", "Packages", "/packages"),
                    SidebarItemDto.child("nav-27", "Domain", "/domain"),
                    SidebarItemDto.child("nav-28", "Purchase Transaction", "/purchase-transaction"),
                    SidebarItemDto.child("nav-29", "Support Tickets", "/tenant-support-tickets"),
                    SidebarItemDto.child("nav-30", "Usage Metrics", "/tenant-usage-metrics")
                ))
            )),
            new SidebarSectionDto("User Management", List.of(
                SidebarItemDto.leaf("nav-99", "Manage Users", "ti ti-users", "/manage-users"),
                SidebarItemDto.leaf("nav-100", "Roles & Permissions", "ti ti-navigation-cog", "/roles-permissions"),
                SidebarItemDto.leaf("nav-101", "Delete Request", "ti ti-flag-question", "/delete-request")
            )),
            new SidebarSectionDto("Settings", List.of(
                SidebarItemDto.group("sub-130", "App Settings", "ti ti-apps", List.of(
                    SidebarItemDto.child("nav-131", "Company Permissions", "/custom-fields-setting")
                )),
                SidebarItemDto.group("sub-134", "System Settings", "ti ti-device-laptop", List.of(
                    SidebarItemDto.child("nav-135", "Email Settings", "/email-settings"),
                    SidebarItemDto.child("nav-138", "GDPR Cookies", "/gdpr-cookies")
                ))
            ))
        );
    }

    private List<SidebarSectionDto> buildCompanyAdminMenu() {
        return List.of(
            new SidebarSectionDto("Main Menu", List.of(
                SidebarItemDto.group("sub-9", "Dashboard", "ti ti-dashboard", List.of(
                    SidebarItemDto.child("nav-1", "Dashboard", "/dashboard"),
                    SidebarItemDto.child("nav-2", "Sales Overview", "/sales-dashboard"),
                    SidebarItemDto.child("nav-3", "Executive Dashboard", "/executive-dashboard")
                )),
                SidebarItemDto.group("sub-company-admin", "Company Admin", "ti ti-building-skyscraper", List.of(
                    SidebarItemDto.child("nav-ca-users", "Manage Users", "/manage-users"),
                    SidebarItemDto.child("nav-ca-roles", "Roles & Permissions", "/roles-permissions"),
                    SidebarItemDto.child("nav-ca-settings", "Company Settings", "/company-settings")
                )),
                SidebarItemDto.group("sub-22", "Applications", "ti ti-brand-airtable", List.of(
                    SidebarItemDto.child("nav-10", "Chat", "/chat"),
                    SidebarItemDto.child("nav-14", "Calendar", "/calendar"),
                    SidebarItemDto.child("nav-15", "Email", "/email"),
                    SidebarItemDto.child("nav-16", "To Do", "/todo")
                ))
            )),
            new SidebarSectionDto("CRM", List.of(
                SidebarItemDto.leaf("nav-32", "Contacts", "ti ti-user-up", "/contacts"),
                SidebarItemDto.leaf("nav-33", "Companies", "ti ti-building-community", "/companies"),
                SidebarItemDto.leaf("nav-36", "Deals", "ti ti-medal", "/deals"),
                SidebarItemDto.leaf("nav-37", "Leads", "ti ti-chart-arcs", "/leads"),
                SidebarItemDto.leaf("nav-38", "Pipeline", "ti ti-timeline-event-exclamation", "/pipeline")
            )),
            new SidebarSectionDto("Reports", List.of(
                SidebarItemDto.leaf("nav-86", "Lead Reports", "ti ti-report-analytics", "/lead-reports"),
                SidebarItemDto.leaf("nav-87", "Deal Reports", "ti ti-report", "/deal-reports")
            ))
        );
    }

    private List<SidebarSectionDto> buildCrmUserMenu() {
        return List.of(
            new SidebarSectionDto("Main Menu", List.of(
                SidebarItemDto.group("sub-9", "Dashboard", "ti ti-dashboard", List.of(
                    SidebarItemDto.child("nav-1", "Dashboard", "/dashboard"),
                    SidebarItemDto.child("nav-4", "Deals Dashboard", "/deals-dashboard"),
                    SidebarItemDto.child("nav-5", "Leads Dashboard", "/leads-dashboard")
                )),
                SidebarItemDto.group("sub-22", "Applications", "ti ti-brand-airtable", List.of(
                    SidebarItemDto.child("nav-10", "Chat", "/chat"),
                    SidebarItemDto.child("nav-14", "Calendar", "/calendar"),
                    SidebarItemDto.child("nav-15", "Email", "/email"),
                    SidebarItemDto.child("nav-16", "To Do", "/todo")
                ))
            )),
            new SidebarSectionDto("CRM", List.of(
                SidebarItemDto.leaf("nav-32", "Contacts", "ti ti-user-up", "/contacts"),
                SidebarItemDto.leaf("nav-33", "Companies", "ti ti-building-community", "/companies"),
                SidebarItemDto.leaf("nav-34", "Account 360", "ti ti-layout-dashboard", "/account-360"),
                SidebarItemDto.leaf("nav-35", "Relationship Map", "ti ti-affiliate", "/relationship-map"),
                SidebarItemDto.leaf("nav-36", "Deals", "ti ti-medal", "/deals"),
                SidebarItemDto.leaf("nav-37", "Leads", "ti ti-chart-arcs", "/leads"),
                SidebarItemDto.leaf("nav-38", "Pipeline", "ti ti-timeline-event-exclamation", "/pipeline"),
                SidebarItemDto.leaf("nav-39", "Proposals", "ti ti-file-star", "/proposals")
            )),
            new SidebarSectionDto("Reports", List.of(
                SidebarItemDto.leaf("nav-86", "Lead Reports", "ti ti-report-analytics", "/lead-reports"),
                SidebarItemDto.leaf("nav-87", "Deal Reports", "ti ti-report", "/deal-reports")
            ))
        );
    }
}
