package com.utn.eventmanager.service.user;

import com.utn.eventmanager.dto.user.EmployeeCreateRequest;
import com.utn.eventmanager.dto.user.UserCreateRequest;
import com.utn.eventmanager.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse createEmployee(EmployeeCreateRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);

    void deactivateUser(Long userId);

    List<UserResponse> getAllUsersIncludingInactive();

}

