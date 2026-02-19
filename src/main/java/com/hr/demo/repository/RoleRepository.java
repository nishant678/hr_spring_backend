package com.hr.demo.repository;

import com.hr.demo.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    boolean existsByNameIgnoreCaseAndCompanyId(String name, Long companyId);

    List<RoleEntity> findAllByCompanyId(Long companyId);
}