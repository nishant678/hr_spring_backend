package com.hr.demo.repository;

import com.hr.demo.entity.HolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<HolidayEntity, Long> {

    List<HolidayEntity> findByCompany_IdOrderByDateDesc(Long companyId);

    List<HolidayEntity> findByCompany_IdAndDateBetweenOrderByDateAsc(Long companyId, LocalDate start, LocalDate end);
}
