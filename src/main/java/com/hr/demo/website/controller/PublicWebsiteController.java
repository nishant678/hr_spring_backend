package com.hr.demo.website.controller;

import com.hr.demo.website.dto.ApiMessageResponse;
import com.hr.demo.website.dto.ContactInquiryRequest;
import com.hr.demo.website.dto.NewsletterRequest;
import com.hr.demo.website.dto.WebsiteHomeResponse;
import com.hr.demo.website.entity.ContactInquiry;
import com.hr.demo.website.entity.JobApplication;
import com.hr.demo.website.entity.WebsiteBlogPost;
import com.hr.demo.website.entity.WebsiteJob;
import com.hr.demo.website.entity.WebsiteServiceItem;
import com.hr.demo.website.service.WebsitePublicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/public/website")
@RequiredArgsConstructor
public class PublicWebsiteController {

    private final WebsitePublicService websiteService;

    @GetMapping("/home-data")
    public ResponseEntity<WebsiteHomeResponse> getHomeData() {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
            .body(websiteService.getHomeData());
    }

    @GetMapping("/services")
    public ResponseEntity<List<WebsiteServiceItem>> getServices() {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
            .body(websiteService.getActiveServices());
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<WebsiteJob>> getJobs() {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
            .body(websiteService.getActiveJobs());
    }

    @GetMapping("/blogs")
    public ResponseEntity<List<WebsiteBlogPost>> getBlogs() {
        return ResponseEntity.ok()
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
            .body(websiteService.getActiveBlogs());
    }

    @PostMapping("/inquiries")
    public ResponseEntity<ApiMessageResponse> submitInquiry(@Valid @RequestBody ContactInquiryRequest request) {
        ContactInquiry saved = websiteService.submitInquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiMessageResponse.ok("Thank you! Your inquiry has been received.", saved.getId()));
    }

    @PostMapping(value = "/careers/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiMessageResponse> applyForJob(
        @RequestParam(required = false) Long jobId,
        @RequestParam String jobTitle,
        @RequestParam String applicantName,
        @RequestParam String email,
        @RequestParam(required = false) String phone,
        @RequestParam(required = false) String linkedinUrl,
        @RequestParam(required = false) String coverNote,
        @RequestParam(required = false) MultipartFile resume
    ) throws IOException {
        JobApplication saved = websiteService.submitJobApplication(
            jobId, jobTitle, applicantName, email, phone, linkedinUrl, coverNote, resume
        );
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiMessageResponse.ok("Application submitted successfully!", saved.getId()));
    }

    @PostMapping("/newsletter")
    public ResponseEntity<ApiMessageResponse> subscribeNewsletter(@Valid @RequestBody NewsletterRequest request) {
        websiteService.subscribeNewsletter(request.email());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiMessageResponse.ok("Subscribed to Work Book HR insights!"));
    }
}
