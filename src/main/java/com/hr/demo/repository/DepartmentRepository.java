package com.hr.demo.repository;

import com.hr.demo.entity.Department;
import com.hr.demo.enums.DepartmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    //    Optional<Department> findByDepartmentCode(String departmentCode);

    //    List<Department> findByCompanyIdAndIsActive(Long companyId, Boolean isActive);

    //    Page<Department> findByCompanyIdAndIsActive(Long companyId, Boolean isActive, Pageable pageable);

    //    @Query("SELECT d FROM Department d WHERE d.companyId = :companyId AND d.parentDepartmentId = :parentDepartmentId AND d.isActive = :isActive")
//    List<Department> findByCompanyIdAndParentDepartmentIdAndIsActive(@Param("companyId") Long companyId, @Param("parentDepartmentId") Long parentDepartmentId, @Param("isActive") Boolean isActive);

    //    @Query("SELECT d FROM Department d WHERE d.companyId = :companyId AND d.status = :status AND d.isActive = :isActive")
//    List<Department> findByCompanyIdAndStatusAndIsActive(@Param("companyId") Long companyId, @Param("status") DepartmentStatus status, @Param("isActive") Boolean isActive);

    //    @Query("SELECT d FROM Department d WHERE d.parentDepartmentId = :parentDepartmentId AND d.isActive = :isActive")
//    List<Department> findByParentDepartmentIdAndIsActive(@Param("parentDepartmentId") Long parentDepartmentId, @Param("isActive") Boolean isActive);

    //    @Query("SELECT COUNT(d) FROM Department d WHERE d.companyId = :companyId AND d.isActive = true")
//    Long countActiveDepartmentsByCompanyId(@Param("companyId") Long companyId);

    //    @Query("SELECT d FROM Department d WHERE d.companyId = :companyId AND d.isActive = true AND " +
//           "(d.name LIKE %:keyword% OR d.departmentCode LIKE %:keyword%)")
//    Page<Department> searchDepartments(@Param("companyId") Long companyId, @Param("keyword") String keyword, Pageable pageable);

    //    boolean existsByDepartmentCode(String departmentCode);

    //    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Department d WHERE d.name = :name AND d.companyId = :companyId AND d.id != :departmentId")
//    boolean existsByNameAndCompanyIdAndIdNot(@Param("name") String name, @Param("companyId") Long companyId, @Param("departmentId") Long departmentId);
}
