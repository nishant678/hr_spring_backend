package com.hr.demo.repository;

import com.hr.demo.entity.LeavePolicy;
import com.hr.demo.enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, Long> {

    //    List<LeavePolicy> findByCompanyIdAndIsActive(Long companyId, Boolean isActive);

    //    @Query("SELECT lp FROM LeavePolicy lp WHERE lp.companyId = :companyId AND lp.leaveType = :leaveType AND lp.isActive = :isActive")
//    List<LeavePolicy> findByCompanyIdAndLeaveTypeAndIsActive(@Param("companyId") Long companyId, @Param("leaveType") LeaveType leaveType, @Param("isActive") Boolean isActive);

    //    List<LeavePolicy> findByCompanyIdAndEffectiveBefore(Long companyId, LocalDateTime dateTime);

    @Query("SELECT lp FROM LeavePolicy lp WHERE lp.companyId = :companyId AND lp.isActive = true AND :employeeRole MEMBER OF lp.applicableRoles")
    List<LeavePolicy> findApplicablePoliciesForRole(@Param("companyId") Long companyId, @Param("employeeRole") String employeeRole);

    @Query("SELECT lp FROM LeavePolicy lp WHERE lp.companyId = :companyId AND lp.isActive = true AND :departmentId MEMBER OF lp.applicableDepartmentIds")
    List<LeavePolicy> findApplicablePoliciesForDepartment(@Param("companyId") Long companyId, @Param("departmentId") Long departmentId);

    @Query("SELECT lp FROM LeavePolicy lp WHERE lp.companyId = :companyId AND lp.isActive = true AND (lp.applicableToAll = true OR :employeeId MEMBER OF lp.applicableEmployeeIds)")
    List<LeavePolicy> findApplicablePoliciesForEmployee(@Param("companyId") Long companyId, @Param("employeeId") Long employeeId);

    @Query("SELECT COUNT(lp) FROM LeavePolicy lp WHERE lp.companyId = :companyId AND lp.isActive = true")
    Long countActivePoliciesByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT lp FROM LeavePolicy lp WHERE lp.companyId = :companyId AND lp.isActive = true AND lp.encashmentAllowed = true")
    List<LeavePolicy> findEncashmentEnabledPolicies(@Param("companyId") Long companyId);

    @Query("SELECT lp FROM LeavePolicy lp WHERE lp.companyId = :companyId AND lp.isActive = true AND lp.carryForwardAllowed = true")
    List<LeavePolicy> findCarryForwardEnabledPolicies(@Param("companyId") Long companyId);

    @Query("SELECT lp FROM LeavePolicy lp WHERE lp.companyId = :companyId AND lp.isActive = true AND lp.approvalRequired = true")
    List<LeavePolicy> findApprovalRequiredPolicies(@Param("companyId") Long companyId);
}
