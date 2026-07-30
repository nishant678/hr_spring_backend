package com.hr.demo.service;

import com.hr.demo.dto.AddCompanyUserRequest;
import com.hr.demo.dto.UpdateCompanyUserRequest;
import com.hr.demo.reaponse.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse addCompanyUser(AddCompanyUserRequest request);
    UserResponse updateCompanyUser(Long id, UpdateCompanyUserRequest request);
    void deleteCompanyUser(Long id);
    UserResponse getCompanyUser(Long id);
    UserResponse getUserProfile(Long id);
    List<UserResponse> getCompanyUsers();
}
