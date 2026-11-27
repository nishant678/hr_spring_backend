package com.hr.demo.website.controller;

import com.hr.demo.website.entity.ContactInquiry;
import com.hr.demo.website.entity.JobApplication;
import com.hr.demo.website.entity.WebsiteBlogPost;
import com.hr.demo.website.entity.WebsiteJob;
import com.hr.demo.website.service.WebsitePublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/website")
@RequiredArgsConstructor
public class AdminWebsiteController {

    private final WebsitePublicService websiteService;

    @GetMapping("/inquiries")
    public ResponseEntity<Page<ContactInquiry>> listInquiries(
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(websiteService.listInquiries(status, PageRequest.of(page, Math.min(size, 100))));
    }

    @PatchMapping("/inquiries/{id}/status")
    public ResponseEntity<ContactInquiry> updateInquiryStatus(
        @PathVariable Long id,
        @RequestParam String status
    ) {
        return ResponseEntity.ok(websiteService.updateInquiryStatus(id, status));
    }

    @GetMapping("/applications")
    public ResponseEntity<Page<JobApplication>> listApplications(
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(websiteService.listApplications(status, PageRequest.of(page, Math.min(size, 100))));
    }

    @PostMapping("/jobs")
    public ResponseEntity<WebsiteJob> saveJob(@RequestBody WebsiteJob job) {
        return ResponseEntity.ok(websiteService.saveJob(job));
    }

    @PostMapping("/blogs")
    public ResponseEntity<WebsiteBlogPost> saveBlog(@RequestBody WebsiteBlogPost post) {
        return ResponseEntity.ok(websiteService.saveBlog(post));
    }
}
