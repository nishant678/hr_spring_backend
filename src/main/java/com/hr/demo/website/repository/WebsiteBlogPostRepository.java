package com.hr.demo.website.repository;

import com.hr.demo.website.entity.WebsiteBlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WebsiteBlogPostRepository extends JpaRepository<WebsiteBlogPost, Long> {
    List<WebsiteBlogPost> findByActiveTrueOrderByCreatedAtDesc();
    Page<WebsiteBlogPost> findByActiveTrueOrderByCreatedAtDesc(Pageable pageable);
}
