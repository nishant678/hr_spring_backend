package com.hr.demo.website.service;

import com.hr.demo.website.dto.ContactInquiryRequest;
import com.hr.demo.website.dto.WebsiteHomeResponse;
import com.hr.demo.website.entity.*;
import com.hr.demo.website.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebsitePublicService {

    private static final Set<String> ALLOWED_RESUME_EXTENSIONS = Set.of(".pdf", ".doc", ".docx");

    private final ContactInquiryRepository inquiryRepository;
    private final WebsiteJobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;
    private final NewsletterSubscriberRepository newsletterRepository;
    private final WebsiteServiceRepository serviceRepository;
    private final WebsitePricingPlanRepository planRepository;
    private final WebsiteBlogPostRepository blogRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Cacheable(value = "websiteHomeData", sync = true)
    @Transactional(readOnly = true)
    public WebsiteHomeResponse getHomeData() {
        return new WebsiteHomeResponse(
            serviceRepository.findByActiveTrueOrderBySortOrderAsc(),
            planRepository.findByActiveTrueOrderBySortOrderAsc(),
            blogRepository.findByActiveTrueOrderByCreatedAtDesc(),
            jobRepository.findByActiveTrueOrderByCreatedAtDesc()
        );
    }

    @Cacheable(value = "websiteServices", sync = true)
    @Transactional(readOnly = true)
    public List<WebsiteServiceItem> getActiveServices() {
        return serviceRepository.findByActiveTrueOrderBySortOrderAsc();
    }

    @Cacheable(value = "websiteJobs", sync = true)
    @Transactional(readOnly = true)
    public List<WebsiteJob> getActiveJobs() {
        return jobRepository.findByActiveTrueOrderByCreatedAtDesc();
    }

    @Cacheable(value = "websiteBlogs", sync = true)
    @Transactional(readOnly = true)
    public List<WebsiteBlogPost> getActiveBlogs() {
        return blogRepository.findByActiveTrueOrderByCreatedAtDesc();
    }

    @Transactional
    public ContactInquiry submitInquiry(ContactInquiryRequest req) {
        ContactInquiry entity = ContactInquiry.builder()
            .firstName(req.firstName().trim())
            .lastName(req.lastName().trim())
            .email(req.email().trim().toLowerCase())
            .company(req.company() != null ? req.company().trim() : null)
            .phone(req.phone() != null ? req.phone().trim() : null)
            .serviceInterest(req.serviceInterest().trim())
            .selectedPlan(req.selectedPlan() != null ? req.selectedPlan().trim() : null)
            .message(req.message().trim())
            .sourcePage(req.sourcePage() != null && !req.sourcePage().isBlank() ? req.sourcePage().trim() : "contact")
            .status("NEW")
            .build();
        return inquiryRepository.save(entity);
    }

    @Transactional
    public JobApplication submitJobApplication(
        Long jobId,
        String jobTitle,
        String applicantName,
        String email,
        String phone,
        String linkedinUrl,
        String coverNote,
        MultipartFile resume
    ) throws IOException {
        String savedResumePath = null;
        if (resume != null && !resume.isEmpty()) {
            String original = resume.getOriginalFilename() != null ? resume.getOriginalFilename() : "resume.pdf";
            String lower = original.toLowerCase();
            String ext = lower.contains(".") ? lower.substring(lower.lastIndexOf('.')) : "";
            if (!ALLOWED_RESUME_EXTENSIONS.contains(ext)) {
                throw new IllegalArgumentException("Only PDF, DOC, and DOCX files are supported.");
            }
            Path dir = Paths.get(uploadDir, "website-resumes").toAbsolutePath().normalize();
            Files.createDirectories(dir);
            String filename = UUID.randomUUID() + ext;
            Path target = dir.resolve(filename);
            resume.transferTo(target.toFile());
            savedResumePath = "website-resumes/" + filename;
        }

        JobApplication app = JobApplication.builder()
            .jobId(jobId)
            .jobTitle(jobTitle.trim())
            .applicantName(applicantName.trim())
            .email(email.trim().toLowerCase())
            .phone(phone != null ? phone.trim() : null)
            .linkedinUrl(linkedinUrl != null ? linkedinUrl.trim() : null)
            .coverNote(coverNote != null ? coverNote.trim() : null)
            .resumePath(savedResumePath)
            .status("RECEIVED")
            .build();
        return applicationRepository.save(app);
    }

    @Transactional
    public NewsletterSubscriber subscribeNewsletter(String email) {
        String normalized = email.trim().toLowerCase();
        return newsletterRepository.findByEmailIgnoreCase(normalized)
            .map(existing -> {
                existing.setActive(true);
                return newsletterRepository.save(existing);
            })
            .orElseGet(() -> newsletterRepository.save(
                NewsletterSubscriber.builder().email(normalized).active(true).build()
            ));
    }

    @Transactional(readOnly = true)
    public Page<ContactInquiry> listInquiries(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return inquiryRepository.findByStatusOrderByCreatedAtDesc(status.trim().toUpperCase(), pageable);
        }
        return inquiryRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional
    public ContactInquiry updateInquiryStatus(Long id, String status) {
        ContactInquiry inquiry = inquiryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Inquiry not found: " + id));
        inquiry.setStatus(status.trim().toUpperCase());
        return inquiryRepository.save(inquiry);
    }

    @Transactional(readOnly = true)
    public Page<JobApplication> listApplications(String status, Pageable pageable) {
        if (status != null && !status.isBlank()) {
            return applicationRepository.findByStatusOrderByAppliedAtDesc(status.trim().toUpperCase(), pageable);
        }
        return applicationRepository.findAllByOrderByAppliedAtDesc(pageable);
    }

    @CacheEvict(value = {"websiteHomeData", "websiteJobs"}, allEntries = true)
    @Transactional
    public WebsiteJob saveJob(WebsiteJob job) {
        return jobRepository.save(job);
    }

    @CacheEvict(value = {"websiteHomeData", "websiteBlogs"}, allEntries = true)
    @Transactional
    public WebsiteBlogPost saveBlog(WebsiteBlogPost post) {
        return blogRepository.save(post);
    }
}
