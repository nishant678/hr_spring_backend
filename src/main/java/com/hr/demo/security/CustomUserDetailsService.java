package com.hr.demo.security;

import com.hr.demo.entity.Employee;
import com.hr.demo.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Employee not found with email: " + email)
                );

        List<SimpleGrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority("ROLE_" + employee.getRole().name())
        );

        return User.builder()
                .username(employee.getEmail())
                .password(employee.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(!employee.getIsActive())
                .credentialsExpired(false)
                .disabled(!employee.getIsActive())
                .build();
    }
}
