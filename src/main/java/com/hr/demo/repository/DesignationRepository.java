package com.hr.demo.repository;

import com.hr.demo.entity.DesignationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<DesignationEntity, Long> {

    @Query(value = "SELECT COUNT(*) > 0 FROM designations d WHERE LOWER(CAST(d.name AS text)) = LOWER(CAST(:name AS text)) AND d.company_id = :companyId", nativeQuery = true)
    boolean existsByNameIgnoreCaseAndCompanyId(@Param("name") String name, @Param("companyId") Long companyId);

    @Query(value = "SELECT d.id, CAST(d.name AS varchar) AS name, CAST(d.description AS varchar) AS description, d.active, d.company_id, d.department_id, d.created_at FROM designations d WHERE d.company_id = :companyId", nativeQuery = true)
    List<DesignationEntity> findAllByCompanyId(@Param("companyId") Long companyId);

    Optional<DesignationEntity> findByIdAndCompany_Id(Long id, Long companyId);

    @Query(value = "SELECT d.id, CAST(d.name AS varchar) AS name, CAST(d.description AS varchar) AS description, d.active, d.company_id, d.department_id, d.created_at FROM designations d WHERE d.company_id = :companyId " +
            "AND (:search IS NULL OR LOWER(CAST(d.name AS text)) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(CAST(d.description AS text)) LIKE LOWER(CONCAT('%', :search, '%')))",
            countQuery = "SELECT COUNT(*) FROM designations d WHERE d.company_id = :companyId " +
            "AND (:search IS NULL OR LOWER(CAST(d.name AS text)) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(CAST(d.description AS text)) LIKE LOWER(CONCAT('%', :search, '%')))",
            nativeQuery = true)
    Page<DesignationEntity> searchByCompany(@Param("companyId") Long companyId,
            @Param("search") String search,
            Pageable pageable);
}
