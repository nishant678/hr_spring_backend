package com.hr.demo.repository;

import com.hr.demo.entity.Leave;
import com.hr.demo.enums.LeaveStatus;
import com.hr.demo.enums.LeaveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    //    @Query("SELECT l FROM Leave l WHERE l.employeeId = :employeeId AND l.status = :status")
//    List<Leave> findByEmployeeIdAndStatus(@Param("employeeId") Long employeeId, @Param("status") LeaveStatus status);

    //    @Query("SELECT l FROM Leave l WHERE l.employeeId = :employeeId AND l.date BETWEEN :startDate AND :endDate")
//    List<Leave> findByEmployeeIdAndDateBetween(@Param("employeeId") Long employeeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    //    @Query("SELECT l FROM Leave l WHERE l.companyId = :companyId AND l.date BETWEEN :startDate AND :endDate")
//    List<Leave> findByCompanyIdAndDateBetween(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    //    Page<Leave> findByCompanyId(Long companyId, Pageable pageable);

    //    Page<Leave> findByEmployeeId(Long employeeId, Pageable pageable);

    @Query("SELECT COUNT(l) FROM Leave l WHERE l.companyId = :companyId AND l.status = 'PENDING'")
    Long countPendingLeavesByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(l) FROM Leave l WHERE l.companyId = :companyId AND l.status = 'APPROVED' AND l.fromDate >= :startDate AND l.fromDate <= :endDate")
    Long countApprovedLeavesByCompanyIdAndDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT l FROM Leave l WHERE l.companyId = :companyId AND l.status = 'PENDING' ORDER BY l.createdAt DESC")
    List<Leave> findPendingLeavesByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT l FROM Leave l WHERE l.employeeId = :employeeId AND l.status = 'PENDING' ORDER BY l.createdAt DESC")
    List<Leave> findPendingLeavesByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT l FROM Leave l WHERE l.companyId = :companyId AND l.leaveType = :leaveType AND l.status = 'APPROVED' AND l.fromDate >= :startDate AND l.toDate <= :endDate")
    List<Leave> findApprovedLeavesByTypeAndDateRange(@Param("companyId") Long companyId, @Param("leaveType") LeaveType leaveType, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(l) FROM Leave l WHERE l.employeeId = :employeeId AND l.leaveType = :leaveType AND l.status = 'APPROVED' AND l.fromDate >= :startDate AND l.toDate <= :endDate")
    Long countApprovedLeavesByEmployeeAndTypeAndDateRange(@Param("employeeId") Long employeeId, @Param("leaveType") LeaveType leaveType, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT l FROM Leave l WHERE l.companyId = :companyId AND l.approvedBy = :managerId AND l.status = 'APPROVED'")
    List<Leave> findLeavesApprovedByManager(@Param("companyId") Long companyId, @Param("managerId") Long managerId);

    @Query("SELECT COUNT(l) FROM Leave l WHERE l.companyId = :companyId AND l.status = 'APPROVED' AND YEAR(l.fromDate) = :year AND MONTH(l.fromDate) = :month")
    Long countApprovedLeavesByMonth(@Param("companyId") Long companyId, @Param("year") Integer year, @Param("month") Integer month);
}
