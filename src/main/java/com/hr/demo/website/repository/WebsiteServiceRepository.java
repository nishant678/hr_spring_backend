package com.hr.demo.website.repository;

import com.hr.demo.website.entity.WebsiteServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WebsiteServiceRepository extends JpaRepository<WebsiteServiceItem, Long> {
    List<WebsiteServiceItem> findByActiveTrueOrderBySortOrderAsc();
}
