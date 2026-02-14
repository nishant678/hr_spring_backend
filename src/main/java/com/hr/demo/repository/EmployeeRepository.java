package com.hr.demo.repository;

import com.hr.demo.entity.Employee;
import com.hr.demo.enums.EmployeeRole;
import com.hr.demo.enums.EmployeeStatus;
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
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmployeeCode(String employeeCode);

    List<Employee> findByCompanyIdAndIsActive(Long companyId, Boolean isActive);

    Page<Employee> findByCompanyIdAndIsActive(Long companyId, Boolean isActive, Pageable pageable);

    List<Employee> findByCompanyIdAndDepartmentIdAndIsActive(Long companyId, Long departmentId, Boolean isActive);

    List<Employee> findByCompanyIdAndRoleAndIsActive(Long companyId, EmployeeRole role, Boolean isActive);

    List<Employee> findByCompanyIdAndStatusAndIsActive(Long companyId, EmployeeStatus status, Boolean isActive);

    @Query("SELECT e FROM Employee e WHERE e.companyId = :companyId AND e.isActive = true AND e.joiningDate <= :date")
    List<Employee> findActiveEmployeesAsOfDate(@Param("companyId") Long companyId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.companyId = :companyId AND e.isActive = true")
    Long countActiveEmployeesByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.companyId = :companyId AND e.departmentId = :departmentId AND e.isActive = true")
    Long countActiveEmployeesByDepartmentId(@Param("companyId") Long companyId, @Param("departmentId") Long departmentId);

    @Query("SELECT e FROM Employee e WHERE e.companyId = :companyId AND e.isActive = true AND " +
           "(e.firstName LIKE %:keyword% OR e.lastName LIKE %:keyword% OR e.email LIKE %:keyword% OR e.employeeCode LIKE %:keyword%)")
    Page<Employee> searchEmployees(@Param("companyId") Long companyId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.companyId = :companyId AND e.isActive = true AND e.lastLogin >= :dateTime")
    List<Employee> findRecentlyActiveEmployees(@Param("companyId") Long companyId, @Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT e FROM Employee e WHERE e.companyId = :companyId AND e.isActive = true AND e.joiningDate BETWEEN :startDate AND :endDate")
    List<Employee> findEmployeesByJoiningDateRange(@Param("companyId") Long companyId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT e FROM Employee e WHERE e.companyId = :companyId AND e.isActive = true AND e.probationEndDate <= :date AND e.confirmationDate IS NULL")
    List<Employee> findEmployeesOnProbation(@Param("companyId") Long companyId, @Param("date") LocalDate date);

    boolean existsByEmail(String email);

    boolean existsByEmployeeCode(String employeeCode);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Employee e WHERE e.email = :email AND e.companyId = :companyId AND e.id != :employeeId")
    boolean existsByEmailAndCompanyIdAndIdNot(@Param("email") String email, @Param("companyId") Long companyId, @Param("employeeId") Long employeeId);

    @Query("SELECT COUNT(e) FROM Employee e WHERE e.isActive = true")
    Long sumTotalActiveEmployees();
}
