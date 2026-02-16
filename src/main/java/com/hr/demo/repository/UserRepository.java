package com.hr.demo.repository;

import com.hr.demo.domain.user.Role;
import com.hr.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByRole(Role role);

    boolean existsByEmail(String email);
}