package com.hr.demo.domain.user;

public enum Role {
    MASTER_ADMIN,
    COMPANY_ADMIN,
    HR,
    EMPLOYEE,
    DISABLED;

    public static Role from(String role) {
        if (role == null || role.isBlank())
            return EMPLOYEE;

        try {
            return Role.valueOf(role.trim().toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid role: " + role);
        }
    }
}