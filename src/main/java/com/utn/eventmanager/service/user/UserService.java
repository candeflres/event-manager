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

    UserResponse getMyProfile(Authentication authentication);

    void deactivateMyAccount(Authentication authentication);

    User getUserFromAuth (Authentication authentication);

    List<UserResponse> getAllUsersIncludingInactive();

}

