package com.hr.demo.website.dto;

import com.hr.demo.website.entity.WebsiteBlogPost;
import com.hr.demo.website.entity.WebsiteJob;
import com.hr.demo.website.entity.WebsitePricingPlan;
import com.hr.demo.website.entity.WebsiteServiceItem;
import java.util.List;

public record WebsiteHomeResponse(
    List<WebsiteServiceItem> services,
    List<WebsitePricingPlan> pricingPlans,
    List<WebsiteBlogPost> blogPosts,
    List<WebsiteJob> jobs
) {}
