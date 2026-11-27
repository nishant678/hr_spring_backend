package com.hr.demo.website.repository;

import com.hr.demo.website.entity.WebsitePricingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WebsitePricingPlanRepository extends JpaRepository<WebsitePricingPlan, Long> {
    List<WebsitePricingPlan> findByActiveTrueOrderBySortOrderAsc();
}
