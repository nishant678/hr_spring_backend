package com.hr.demo.hr.repository;

import com.hr.demo.domain.asset.AssetStatus;
import com.hr.demo.hr.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<AssetEntity, Long> {

    List<AssetEntity> findByCompany_IdOrderByCreatedAtDesc(Long companyId);

    List<AssetEntity> findByCompany_IdAndStatusOrderByCreatedAtDesc(Long companyId, AssetStatus status);

    List<AssetEntity> findByAssignedTo_IdOrderByCreatedAtDesc(Long userId);

    long countByCompany_IdAndStatus(Long companyId, AssetStatus status);
}
