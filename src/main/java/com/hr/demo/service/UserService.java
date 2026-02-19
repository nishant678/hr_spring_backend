package com.hr.demo.service;

import com.hr.demo.dto.CreateUserRequest;
import com.hr.demo.reaponse.UserResponse;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
}
