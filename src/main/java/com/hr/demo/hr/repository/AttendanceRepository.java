package com.hr.demo.hr.repository;

import com.hr.demo.hr.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    List<AttendanceEntity> findByUser_IdOrderByDateDescCheckInTimeDesc(Long userId);

    List<AttendanceEntity> findByCompany_IdOrderByDateDescCheckInTimeDesc(Long companyId);

    Optional<AttendanceEntity> findByUser_IdAndDate(Long userId, LocalDate date);

    List<AttendanceEntity> findByCompany_IdAndDate(Long companyId, LocalDate date);

    List<AttendanceEntity> findByCompany_IdAndDateBetweenOrderByDateDesc(Long companyId, LocalDate start, LocalDate end);

    List<AttendanceEntity> findByUser_IdAndDateBetween(Long userId, LocalDate start, LocalDate end);

    long countByCompany_IdAndDateAndStatus(Long companyId, LocalDate date, String status);
}
