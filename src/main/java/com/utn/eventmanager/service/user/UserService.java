package com.utn.eventmanager.service.user;

import com.utn.eventmanager.dto.user.EmployeeCreateRequest;
import com.utn.eventmanager.dto.user.UserCreateRequest;
import com.utn.eventmanager.dto.user.UserResponse;
import com.utn.eventmanager.model.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse createEmployee(EmployeeCreateRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);

    void deactivateUser(Long userId);

    User getUserFromAuth (Authentication authentication);

    List<UserResponse> getAllUsersIncludingInactive();

}

