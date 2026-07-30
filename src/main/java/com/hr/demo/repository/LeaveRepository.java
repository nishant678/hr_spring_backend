package com.hr.demo.repository;

import com.hr.demo.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveEntity, Long> {

    List<LeaveEntity> findByCompany_IdOrderByCreatedAtDesc(Long companyId);

    List<LeaveEntity> findByUser_IdOrderByCreatedAtDesc(Long userId);

    List<LeaveEntity> findByCompany_IdAndStatusOrderByCreatedAtDesc(Long companyId, String status);

    @org.springframework.data.jpa.repository.Query("SELECT l FROM LeaveEntity l WHERE l.user.id = :userId AND l.status = 'APPROVED' AND l.fromDate <= :endDate AND l.toDate >= :startDate")
    List<LeaveEntity> findApprovedLeavesByUserAndMonth(@org.springframework.data.repository.query.Param("userId") Long userId, @org.springframework.data.repository.query.Param("startDate") LocalDate startDate, @org.springframework.data.repository.query.Param("endDate") LocalDate endDate);
}
