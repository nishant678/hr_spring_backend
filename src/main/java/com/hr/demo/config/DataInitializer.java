package com.hr.demo.config;

import com.hr.demo.domain.user.Role;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        boolean superAdminExists = userRepository.existsByRole(Role.MASTER_ADMIN);

        if (!superAdminExists) {
            UserEntity master = UserEntity.builder()
                    .email("master@system.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.MASTER_ADMIN)
                    .build();

            userRepository.save(master);
            System.out.println("🔥 MASTER SUPER ADMIN CREATED");
            System.out.println("Email: master@system.com");
            System.out.println("Password: admin123");
        }
    }
}