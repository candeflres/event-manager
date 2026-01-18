package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.user.UserCreateRequest;
import com.utn.eventmanager.dto.user.UserResponse;
import com.utn.eventmanager.service.user.UserService;
import jakarta.validation.Valid;
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
    @DeleteMapping("/{id}")
    public void deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
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