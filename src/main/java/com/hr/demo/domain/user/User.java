package com.hr.demo.domain.user;

public class User {
    private Long id;
    private String email;
    private String password;
    private Role role;

    public User(Long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
