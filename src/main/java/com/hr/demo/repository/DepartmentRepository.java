package com.hr.demo.repository;

import com.hr.demo.entity.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    boolean existsByNameIgnoreCaseAndCompanyId(String name, Long companyId);

    List<DepartmentEntity> findAllByCompanyId(Long companyId);

    Optional<DepartmentEntity> findByIdAndCompany_Id(Long id, Long companyId);

    @Query("SELECT d FROM DepartmentEntity d WHERE d.company.id = :companyId " +
            "AND (:search IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(d.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<DepartmentEntity> searchByCompany(@Param("companyId") Long companyId,
            @Param("search") String search,
            Pageable pageable);
}
