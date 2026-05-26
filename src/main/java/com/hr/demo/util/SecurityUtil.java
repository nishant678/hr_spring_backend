package com.hr.demo.util;

import com.hr.demo.entity.UserEntity;
import com.hr.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityUtil {

    private final UserRepository userRepository;

    public Optional<UserEntity> getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            log.warn("getCurrentUser: Auth is null or not authenticated");
            return Optional.empty();
        }

        String email = null;
        if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (auth.getPrincipal() instanceof String principalName) {
            email = principalName;
        }

        if (email == null || email.isBlank() || "anonymousUser".equals(email)) {
            log.warn("getCurrentUser: Email is null, blank, or anonymous");
            return Optional.empty();
        }

        log.info("getCurrentUser: Looking up user with email: {}", email);
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            log.info("getCurrentUser: User found - id: {}, company: {}", userEntity.getId(),
                    userEntity.getCompany() != null ? userEntity.getCompany().getId() : "NULL");
            if (userEntity.getCompany() == null) {
                log.warn("getCurrentUser: User {} has no company assigned!", email);
            }
        } else {
            log.warn("getCurrentUser: User not found in database with email: {}", email);
        }

        return user;
    }
}
