package com.hr.demo.website.dto;

public record ApiMessageResponse(boolean success, String message, Long id) {
    public static ApiMessageResponse ok(String message, Long id) {
        return new ApiMessageResponse(true, message, id);
    }

    public static ApiMessageResponse ok(String message) {
        return new ApiMessageResponse(true, message, null);
    }
}
