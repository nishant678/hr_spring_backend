package com.hr.demo.reaponse;

import com.hr.demo.domain.company.CompanyStatus;
import com.hr.demo.hr.entity.UserEntity;
import java.util.Map;

public record MasterDataResponse(Long userId, Long companyId, String companyName,
                                 Map<String, Boolean> attendance,
                                 Map<String, Boolean> permissions) {
    public static MasterDataResponse forUser(UserEntity user) {
        var company = user.getCompany();
        boolean enabled = company != null && company.getStatus() == CompanyStatus.ACTIVE;
        return new MasterDataResponse(user.getId(), company == null ? null : company.getId(),
                company == null ? null : company.getCompanyName(),
                Map.of("enabled", enabled,
                        "face_attendense", company != null && company.isFaceVerificationAttendance()),
                Map.of("task", enabled && company.isTaskEnabled()));
    }
}
