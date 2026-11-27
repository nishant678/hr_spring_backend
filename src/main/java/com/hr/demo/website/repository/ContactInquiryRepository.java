package com.hr.demo.website.repository;

import com.hr.demo.website.entity.ContactInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {
    Page<ContactInquiry> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<ContactInquiry> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);
}
