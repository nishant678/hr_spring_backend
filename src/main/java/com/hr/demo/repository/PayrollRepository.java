package com.hr.demo.repository;

import com.hr.demo.entity.PayrollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollEntity, Long> {

    List<PayrollEntity> findByCompany_IdAndYearAndMonthOrderByUser_IdAsc(Long companyId, int year, int month);

    Optional<PayrollEntity> findByUser_IdAndYearAndMonth(Long userId, int year, int month);

    long countByCompany_IdAndYearAndMonth(Long companyId, int year, int month);

    List<PayrollEntity> findByCompany_IdAndYearAndMonthAndStatus(Long companyId, int year, int month, String status);
}
