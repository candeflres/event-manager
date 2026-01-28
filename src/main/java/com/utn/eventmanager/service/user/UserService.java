package com.utn.eventmanager.service.user;

import com.utn.eventmanager.dto.user.*;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.UserRole;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse createEmployee(Authentication authentication,EmployeeCreateRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long userId);
    void deactivateUserByAdmin(Authentication authentication,
                               Long userId);
    UserResponse getMyProfile(Authentication authentication);

    void deactivateMyAccount(Authentication authentication);

    void deactivateUser(Long userId);

    User getUserFromAuth (Authentication authentication);

    List<UserResponse> getAllUsersIncludingInactive();

    UserResponse updateMyProfile(Authentication authentication, UserUpdateRequest request);

    UserResponse updateUserByAdmin(Authentication authentication, Long userId, UserUpdateRequest request);

    List<UserResponse> getEmployees();

    void changeMyPassword(Authentication authentication, ChangePasswordRequest request);
}

