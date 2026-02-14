package com.hr.demo.repository;

import com.hr.demo.entity.HolidayCalendar;
import com.hr.demo.enums.HolidayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayCalendarRepository extends JpaRepository<HolidayCalendar, Long> {

    List<HolidayCalendar> findByCompanyIdAndDateBetween(Long companyId, LocalDate startDate, LocalDate endDate);

    List<HolidayCalendar> findByCompanyIdAndYear(Long companyId, Integer year);

    List<HolidayCalendar> findByCompanyIdAndDate(Long companyId, LocalDate date);

    List<HolidayCalendar> findByCompanyIdAndHolidayType(Long companyId, HolidayType holidayType);

    @Query("SELECT h FROM HolidayCalendar h WHERE h.companyId = :companyId AND h.isRecurring = true")
    List<HolidayCalendar> findRecurringHolidaysByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT h FROM HolidayCalendar h WHERE h.companyId = :companyId AND h.isOptional = true AND h.date BETWEEN :startDate AND :endDate")
    List<HolidayCalendar> findOptionalHolidaysByCompanyIdAndDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT h FROM HolidayCalendar h WHERE h.companyId = :companyId AND h.isHalfDay = true AND h.date BETWEEN :startDate AND :endDate")
    List<HolidayCalendar> findHalfDayHolidaysByCompanyIdAndDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(h) FROM HolidayCalendar h WHERE h.companyId = :companyId AND h.date BETWEEN :startDate AND :endDate")
    Long countHolidaysByCompanyIdAndDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT h FROM HolidayCalendar h WHERE h.companyId = :companyId AND h.applicableToAll = true")
    List<HolidayCalendar> findApplicableToAllHolidaysByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT h FROM HolidayCalendar h WHERE h.companyId = :companyId AND :employeeId MEMBER OF h.applicableEmployeeIds")
    List<HolidayCalendar> findHolidaysForEmployee(@Param("companyId") Long companyId, @Param("employeeId") Long employeeId);
}
