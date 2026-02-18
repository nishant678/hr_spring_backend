package com.hr.demo.repository;
import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.domain.user.SubscriptionPlan;
import com.hr.demo.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for CompanyEntity.
 */
@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    // ================= CREATE VALIDATION =================

    // case insensitive duplicate prevention (IMPORTANT)
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhone(String phone);


    // ================= LOGIN / TENANT DETECTION =================

    // detect company during login using email domain
    Optional<CompanyEntity> findByEmailIgnoreCase(String email);

    // allow login only if ACTIVE
    Optional<CompanyEntity> findByIdAndStatus(Long id, CompanyStatus status);


    // ================= ADMIN PANEL =================

    // listing with filter
    List<CompanyEntity> findAllByStatus(CompanyStatus status);

    Page<CompanyEntity> findAllByStatus(CompanyStatus status, Pageable pageable);


    // ================= SUBSCRIPTION MANAGEMENT =================

    // expired companies
    List<CompanyEntity> findBySubscriptionEndBeforeAndStatus(
            LocalDate date,
            CompanyStatus status
    );

    // expiring soon (cron reminder)
    List<CompanyEntity> findBySubscriptionEndBetweenAndStatus(
            LocalDate start,
            LocalDate end,
            CompanyStatus status
    );


    // ================= BILLING / ANALYTICS =================

    long countByStatus(CompanyStatus status);

    long countBySubscriptionPlan(SubscriptionPlan plan);
}