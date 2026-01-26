package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.user.ChangePasswordRequest;
import com.utn.eventmanager.dto.user.UserCreateRequest;
import com.utn.eventmanager.dto.user.UserResponse;
import com.utn.eventmanager.dto.user.UserUpdateRequest;
import com.utn.eventmanager.service.user.UserService;
import jakarta.validation.Valid;
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

    @PutMapping("/me/password")
    public void changeMyPassword(
            Authentication authentication,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        userService.changeMyPassword(authentication, request);
    }

    @PutMapping("/me/deactivate")
    public void deactivateMyAccount(Authentication authentication) {
        userService.deactivateMyAccount(authentication);
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
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    //---------------------------------------//
    //----------- LOW USER LOGIC -----------//
    //-------------------------------------//
    @GetMapping("/me")
    public UserResponse getMyProfile(Authentication authentication) {
        return userService.getMyProfile(authentication);
    }



    //-----------------------------------------------------------//
    //----------- VIEW ALL ACTIVE AND INACTIVE USERS -----------//
    //---------------------------------------------------------//
    // View all active and inactive users
    @GetMapping("/all")
    public List<UserResponse> getAllUsersIncludingInactive() {
        return userService.getAllUsersIncludingInactive();
    }








}