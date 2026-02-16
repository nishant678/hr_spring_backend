package com.hr.demo.config;

import com.hr.demo.domain.user.Role;
import com.hr.demo.entity.UserEntity;
import com.hr.demo.repository.UserRepository;
import com.hr.demo.utils.PasswordEncoderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {

        boolean superAdminExists = userRepository.existsByRole(Role.MASTER_ADMIN);

        if (!superAdminExists) {

            UserEntity master = new UserEntity();
            master.setEmail("master@system.com");
            master.setPassword(PasswordEncoderUtil.encode("admin123"));
            master.setRole(Role.MASTER_ADMIN);

            userRepository.save(master);

            System.out.println("🔥 MASTER SUPER ADMIN CREATED");
            System.out.println("Email: master@system.com");
            System.out.println("Password: admin123");
        }
    }
}