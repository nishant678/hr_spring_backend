package com.hr.demo.repository;

import com.hr.demo.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<LeaveEntity, Long> {

    List<LeaveEntity> findByCompany_IdOrderByCreatedAtDesc(Long companyId);

    List<LeaveEntity> findByUser_IdOrderByCreatedAtDesc(Long userId);

    List<LeaveEntity> findByCompany_IdAndStatusOrderByCreatedAtDesc(Long companyId, String status);
}
