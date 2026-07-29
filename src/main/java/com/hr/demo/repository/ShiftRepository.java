package com.hr.demo.repository;

import com.hr.demo.entity.ShiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftEntity, Long> {

    List<ShiftEntity> findByCompany_IdOrderByNameAsc(Long companyId);
}
