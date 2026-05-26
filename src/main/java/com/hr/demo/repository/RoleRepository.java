package com.hr.demo.repository;

import com.hr.demo.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    boolean existsByNameIgnoreCaseAndCompanyId(String name, Long companyId);

    List<RoleEntity> findAllByCompanyId(Long companyId);

    @Query("SELECT r FROM RoleEntity r WHERE r.company.id = :companyId " +
            "AND (:search IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(r.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<RoleEntity> searchByCompany(@Param("companyId") Long companyId,
            @Param("search") String search,
            Pageable pageable);
}