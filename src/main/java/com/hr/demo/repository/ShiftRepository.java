package com.hr.demo.repository;

import com.hr.demo.entity.Shift;
import com.hr.demo.enums.ShiftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {

    Optional<Shift> findByShiftCode(String shiftCode);

    //    List<Shift> findByCompanyIdAndIsActive(Long companyId, Boolean isActive);

    //    Page<Shift> findByCompanyIdAndIsActive(Long companyId, Boolean isActive, Pageable pageable);

    @Query("SELECT COUNT(s) FROM Shift s WHERE s.companyId = :companyId AND s.isActive = true")
    Long countActiveShiftsByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT s FROM Shift s WHERE s.companyId = :companyId AND s.isActive = true AND " +
           "(s.name LIKE %:keyword% OR s.shiftCode LIKE %:keyword%)")
    Page<Shift> searchShifts(@Param("companyId") Long companyId, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.shiftId = :shiftId AND e.isActive = true")
    List<Shift> findEmployeesByShiftId(@Param("shiftId") Long shiftId);

    boolean existsByShiftCode(String shiftCode);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Shift s WHERE s.name = :name AND s.companyId = :companyId AND s.id != :shiftId")
    boolean existsByNameAndCompanyIdAndIdNot(@Param("name") String name, @Param("companyId") Long companyId, @Param("shiftId") Long shiftId);
}
