package com.hr.demo.repository;

import com.hr.demo.entity.Attendance;
import com.hr.demo.enums.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    Optional<Attendance> findByEmployeeIdAndDateAndCheckInIsNotNull(Long employeeId, LocalDate date);

    List<Attendance> findByEmployeeIdAndDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<Attendance> findByCompanyIdAndDate(Long companyId, LocalDate date);

    List<Attendance> findByCompanyIdAndDateBetween(Long companyId, LocalDate startDate, LocalDate endDate);

    Page<Attendance> findByCompanyIdAndDate(Long companyId, LocalDate date, Pageable pageable);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.companyId = :companyId AND a.date = :date AND a.status = 'PRESENT'")
    Long countPresentEmployeesByDate(@Param("companyId") Long companyId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.companyId = :companyId AND a.date = :date AND a.status = 'ABSENT'")
    Long countAbsentEmployeesByDate(@Param("companyId") Long companyId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.companyId = :companyId AND a.date = :date AND a.status = 'LATE'")
    Long countLateEmployeesByDate(@Param("companyId") Long companyId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.companyId = :companyId AND a.date = :date AND a.status = 'HALF_DAY'")
    Long countHalfDayEmployeesByDate(@Param("companyId") Long companyId, @Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.companyId = :companyId AND a.date BETWEEN :startDate AND :endDate ORDER BY a.date DESC")
    List<Attendance> findAttendanceByDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.employeeId = :employeeId AND a.date BETWEEN :startDate AND :endDate")
    Long countAttendanceByEmployeeAndDateRange(@Param("employeeId") Long employeeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM Attendance a WHERE a.companyId = :companyId AND a.checkIn IS NULL AND a.date = :date")
    List<Attendance> findEmployeesWithoutCheckIn(@Param("companyId") Long companyId, @Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.companyId = :companyId AND a.checkOut IS NULL AND a.date = :date")
    List<Attendance> findEmployeesWithoutCheckOut(@Param("companyId") Long companyId, @Param("date") LocalDate date);
}
