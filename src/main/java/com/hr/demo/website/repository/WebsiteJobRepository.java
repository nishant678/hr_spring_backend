package com.hr.demo.website.repository;

import com.hr.demo.website.entity.WebsiteJob;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WebsiteJobRepository extends JpaRepository<WebsiteJob, Long> {
    List<WebsiteJob> findByActiveTrueOrderByCreatedAtDesc();
}
