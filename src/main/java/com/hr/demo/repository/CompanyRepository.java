package com.hr.demo.repository;
import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    // 🔐 Used while creating company (duplicate prevention)
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    // 🔐 Used during login (tenant detection)
    Optional<CompanyEntity> findByEmail(String email);

    // 🔐 Only active companies allowed login
    Optional<CompanyEntity> findByIdAndStatus(Long id, CompanyStatus status);

    // 📊 Admin panel listing
    List<CompanyEntity> findAllByStatus(CompanyStatus status);

    // ⏰ Subscription expiry check (cron job)
    List<CompanyEntity> findBySubscriptionEndBefore(LocalDate date);

}