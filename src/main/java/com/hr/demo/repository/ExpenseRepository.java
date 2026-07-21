package com.hr.demo.repository;

import com.hr.demo.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Long> {

    List<ExpenseEntity> findByCompany_IdOrderByCreatedAtDesc(Long companyId);

    List<ExpenseEntity> findByUser_IdOrderByCreatedAtDesc(Long userId);

    List<ExpenseEntity> findByCompany_IdAndStatusOrderByCreatedAtDesc(Long companyId, String status);
}
