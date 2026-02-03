package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.user.*;
import com.utn.eventmanager.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //----------------------------------------------//
    //----------- CUSTOMER REGISTRATION -----------//
    //--------------------------------------------//
    @PostMapping
    public UserResponse createUser(@RequestBody @Valid UserCreateRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/me")
    public UserResponse updateMyProfile(
            Authentication authentication,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        return userService.updateMyProfile(authentication, request);
    }

    @PutMapping("/{id}/activate")
    public void activateUserByAdmin(
            Authentication authentication,
            @PathVariable Long id
    ) {
        userService.activateUserByAdmin(authentication, id);
    }

    @PutMapping("/me/password")
    public void changeMyPassword(
            Authentication authentication,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        userService.changeMyPassword(authentication, request);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivateUserByAdmin(
            Authentication authentication,
            @PathVariable Long id
    ) {
        userService.deactivateUserByAdmin(authentication, id);
    }
    //----------------------------------------------//
    //----------- LIST ACTIVE USERS ---------------//
    //--------------------------------------------//
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    //----------------------------------------------//
    //----------- SEARCH USER BY ID ---------------//
    //--------------------------------------------//
    @GetMapping("/employees")
    public List<UserResponse> getEmployees() {
        return userService.getEmployees();
    }
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateUserByAdmin(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        return userService.updateUserByAdmin(authentication, id, request);
    }

    @PutMapping("/me/deactivate")
    public ResponseEntity<Void> deactivateMyAccount(Authentication authentication) {
        userService.deactivateMyAccount(authentication);
        return ResponseEntity.noContent().build();
    }

    //----------------------------------------------//
//----------- CREATE EMPLOYEE (ADMIN) ----------//
//----------------------------------------------//
    @PostMapping("/employees")
    public UserResponse createEmployee(
            Authentication authentication,
            @RequestBody @Valid EmployeeCreateRequest request
    ) {
        return userService.createEmployee(authentication, request);
    }

    //---------------------------------------//
    //----------- LOW USER LOGIC -----------//
    //-------------------------------------//
    @GetMapping("/me")
    public UserResponse getMyProfile(Authentication authentication) {
        return userService.getMyProfile(authentication);
    }

    //----------------------------------------------//
//----------- LIST EMPLOYEES (ADMIN) -----------//
//----------------------------------------------//


    //-----------------------------------------------------------//
    //----------- VIEW ALL ACTIVE AND INACTIVE USERS -----------//
    //---------------------------------------------------------//
    // View all active and inactive users
    @GetMapping("/all")
    public List<UserResponse> getAllUsersIncludingInactive() {
        return userService.getAllUsersIncludingInactive();
    }

}