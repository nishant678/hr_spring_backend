package com.hr.demo.reaponse;

import com.hr.demo.domain.user.Role;
import com.hr.demo.hr.entity.UserEntity;
import com.hr.demo.security.JwtService;
import lombok.Getter;

@Getter
public class AuthResponse {
    private String token;
    private String refreshToken;
    private long expiresAt;
    private String email;
    private String role;
    private Long id;
    private Long companyId;
    private boolean faceRegistered;

    public AuthResponse(String token, String refreshToken, long expiresAt,
                        String email, Role role, Long id, Long companyId, boolean faceRegistered) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.email = email;
        this.role = role.name();
        this.id = id;
        this.companyId = companyId;
        this.faceRegistered = faceRegistered;
    }

    public static AuthResponse fromPair(JwtService.TokenPair pair, UserEntity user) {
        Long companyId = user.getCompany() != null ? user.getCompany().getId() : null;
        return new AuthResponse(pair.accessToken(), pair.refreshToken(), pair.expiresAt(),
                user.getEmail(), user.getRole(), user.getId(), companyId, user.isFaceRegistered());
    }
}
