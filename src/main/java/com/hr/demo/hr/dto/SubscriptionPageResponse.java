package com.hr.demo.hr.dto;

import com.hr.demo.reaponse.CompanyResponse;
import java.util.List;

public record SubscriptionPageResponse(
    List<CompanyResponse> items,
    int page,
    int size,
    long totalElements,
    int totalPages,
    long totalSubscribers,
    long activeSubscribers,
    long inactiveSubscribers,
    long totalRevenue
) {}
