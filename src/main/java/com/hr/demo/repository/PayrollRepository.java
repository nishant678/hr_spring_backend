package com.hr.demo.repository;

import com.hr.demo.entity.Payroll;
import com.hr.demo.enums.PayrollStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    //    List<Payroll> findByEmployeeId(Long employeeId);

    //    @Query("SELECT p FROM Payroll p WHERE p.employeeId = :employeeId AND p.payrollMonth = :month AND p.payrollYear = :year")
//    List<Payroll> findByEmployeeIdAndPayrollMonthAndPayrollYear(@Param("employeeId") Long employeeId, @Param("month") Integer month, @Param("year") Integer year);

    //    @Query("SELECT p FROM Payroll p WHERE p.companyId = :companyId AND p.payrollMonth = :month AND p.payrollYear = :year")
//    List<Payroll> findByCompanyIdAndPayrollMonthAndPayrollYear(@Param("companyId") Long companyId, @Param("month") Integer month, @Param("year") Integer year);

    //    Page<Payroll> findByCompanyId(Long companyId, Pageable pageable);

    //    Page<Payroll> findByEmployeeId(Long employeeId, Pageable pageable);

    //    @Query("SELECT COUNT(p) FROM Payroll p WHERE p.companyId = :companyId AND p.payrollMonth = :month AND p.payrollYear = :year")
//    Long countPayrollByCompanyAndMonth(@Param("companyId") Long companyId, @Param("month") Integer month, @Param("year") Integer year);

    //    @Query("SELECT SUM(p.netSalary) FROM Payroll p WHERE p.companyId = :companyId AND p.payrollMonth = :month AND p.payrollYear = :year AND p.status = 'PAID'")
//    Double sumTotalPayrollByCompanyAndMonth(@Param("companyId") Long companyId, @Param("month") Integer month, @Param("year") Integer year);

    //    @Query("SELECT AVG(p.netSalary) FROM Payroll p WHERE p.companyId = :companyId AND p.payrollMonth = :month AND p.payrollYear = :year AND p.status = 'PAID'")
//    Double averageSalaryByCompanyAndMonth(@Param("companyId") Long companyId, @Param("month") Integer month, @Param("year") Integer year);

    //    @Query("SELECT p FROM Payroll p WHERE p.companyId = :companyId AND p.status = 'PENDING_APPROVAL'")
//    List<Payroll> findPendingPayrollsByCompanyId(@Param("companyId") Long companyId);

    //    @Query("SELECT p FROM Payroll p WHERE p.companyId = :companyId AND p.status = 'PROCESSED'")
//    List<Payroll> findProcessedPayrollsByCompanyId(@Param("companyId") Long companyId);

    //    @Query("SELECT p FROM Payroll p WHERE p.companyId = :companyId AND p.status = 'PAID' AND p.payrollMonth = :month AND p.payrollYear = :year")
//    List<Payroll> findPaidPayrollsByCompanyAndMonth(@Param("companyId") Long companyId, @Param("month") Integer month, @Param("year") Integer year);

    //    @Query("SELECT COUNT(p) FROM Payroll p WHERE p.companyId = :companyId AND p.status = 'PAID' AND p.payrollMonth = :month AND p.payrollYear = :year")
//    Long countPaidPayrollsByCompanyAndMonth(@Param("companyId") Long companyId, @Param("month") Integer month, @Param("year") Integer year);
}
