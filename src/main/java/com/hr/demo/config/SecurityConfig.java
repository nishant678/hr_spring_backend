package com.hr.demo.config;

import com.hr.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {

        http
                // Disable CSRF (JWT is stateless)
                .csrf(csrf -> csrf.disable())

                // No session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authorization rules with role-based access
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/health",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password",
                                "/actuator/health"
                        ).permitAll()

                        // Super Admin endpoints
                        .requestMatchers("/api/super-admin/**").hasRole("SUPER_ADMIN")

                        // Company Admin endpoints
                        .requestMatchers("/api/company-admin/**").hasAnyRole("SUPER_ADMIN", "COMPANY_ADMIN")

                        // HR Manager endpoints
                        .requestMatchers("/api/hr/**").hasAnyRole("SUPER_ADMIN", "COMPANY_ADMIN", "HR_MANAGER")

                        // Employee endpoints
                        .requestMatchers("/api/employee/**").hasAnyRole("SUPER_ADMIN", "COMPANY_ADMIN", "HR_MANAGER", "EMPLOYEE")

                        // Common authenticated endpoints
                        .requestMatchers("/api/common/**").authenticated()

                        // Any other request needs authentication
                        .anyRequest().authenticated()
                )

                // Add JWT filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Disable default login form
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
