package com.hr.demo.website.repository;

import com.hr.demo.website.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Page<JobApplication> findAllByOrderByAppliedAtDesc(Pageable pageable);
    Page<JobApplication> findByStatusOrderByAppliedAtDesc(String status, Pageable pageable);
}
