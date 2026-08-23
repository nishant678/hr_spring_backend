package com.hr.demo.hr.repository;

import com.hr.demo.hr.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query(value = "SELECT COUNT(*) > 0 FROM roles r WHERE LOWER(CAST(r.name AS text)) = LOWER(CAST(:name AS text)) AND r.company_id = :companyId", nativeQuery = true)
    boolean existsByNameIgnoreCaseAndCompanyId(@Param("name") String name, @Param("companyId") Long companyId);

    @Query(value = "SELECT r.id, CAST(r.name AS varchar) AS name, CAST(r.description AS varchar) AS description, r.active, r.company_id, r.created_at FROM roles r WHERE r.company_id = :companyId", nativeQuery = true)
    List<RoleEntity> findAllByCompanyId(@Param("companyId") Long companyId);

    Optional<RoleEntity> findByIdAndCompany_Id(Long id, Long companyId);

    @Query(value = "SELECT r.id, CAST(r.name AS varchar) AS name, CAST(r.description AS varchar) AS description, r.active, r.company_id, r.created_at FROM roles r WHERE r.company_id = :companyId " +
            "AND (:search IS NULL OR LOWER(CAST(r.name AS text)) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(CAST(r.description AS text)) LIKE LOWER(CONCAT('%', :search, '%')))",
            countQuery = "SELECT COUNT(*) FROM roles r WHERE r.company_id = :companyId " +
            "AND (:search IS NULL OR LOWER(CAST(r.name AS text)) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(CAST(r.description AS text)) LIKE LOWER(CONCAT('%', :search, '%')))",
            nativeQuery = true)
    Page<RoleEntity> searchByCompany(@Param("companyId") Long companyId,
            @Param("search") String search,
            Pageable pageable);
}
