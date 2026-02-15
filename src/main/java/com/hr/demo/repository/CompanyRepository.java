package com.hr.demo.repository;

import com.hr.demo.entity.Company;
import com.hr.demo.enums.CompanyStatus;
import com.hr.demo.enums.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    //    Optional<Company> findByEmail(String email);

    //    Optional<Company> findByCompanyCode(String companyCode);

    //    List<Company> findByStatus(CompanyStatus status);

    @Query("SELECT c FROM Company c WHERE c.status = :status")
    Page<Company> findByStatusWithPagination(@Param("status") CompanyStatus status, Pageable pageable);

    //    List<Company> findBySubscriptionPlan(SubscriptionPlan subscriptionPlan);

    @Query("SELECT c FROM Company c WHERE c.subscriptionPlan = :plan")
    Page<Company> findBySubscriptionPlanWithPagination(@Param("plan") SubscriptionPlan plan, Pageable pageable);

    @Query("SELECT c FROM Company c WHERE c.subscriptionEnd <= :dateTime AND c.status = 'ACTIVE'")
    List<Company> findCompaniesWithExpiredSubscription(@Param("dateTime") LocalDateTime dateTime);

    @Query("SELECT COUNT(c) FROM Company c WHERE c.status = :status")
    Long countByStatus(@Param("status") CompanyStatus status);

    @Query("SELECT COUNT(c) FROM Company c WHERE c.subscriptionPlan = :plan AND c.status = 'ACTIVE'")
    Long countActiveCompaniesBySubscriptionPlan(@Param("plan") SubscriptionPlan plan);

    @Query("SELECT c FROM Company c WHERE c.isActive = true AND (c.name LIKE %:keyword% OR c.email LIKE %:keyword% OR c.companyCode LIKE %:keyword%)")
    Page<Company> searchActiveCompanies(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT c FROM Company c WHERE c.isActive = true AND c.currentEmployees >= c.maxEmployees")
    List<Company> findCompaniesAtEmployeeLimit();

    @Query("SELECT SUM(c.currentEmployees) FROM Company c WHERE c.status = 'ACTIVE'")
    Long sumTotalActiveEmployees();

    boolean existsByEmail(String email);

    boolean existsByCompanyCode(String companyCode);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Company c WHERE c.email = :email AND c.id != :companyId")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("companyId") Long companyId);
}
