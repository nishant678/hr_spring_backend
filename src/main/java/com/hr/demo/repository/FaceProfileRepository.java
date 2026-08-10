package com.hr.demo.repository;

import com.hr.demo.entity.FaceProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FaceProfileRepository extends JpaRepository<FaceProfileEntity, Long> {

    @Query("SELECT f FROM FaceProfileEntity f WHERE f.employeeId = :employeeId AND f.active = true")
    Optional<FaceProfileEntity> findActiveByEmployeeId(String employeeId);

    @Modifying
    @Query("UPDATE FaceProfileEntity f SET f.active = false WHERE f.employeeId = :employeeId AND f.active = true")
    int deactivateAllForEmployee(String employeeId);
}
