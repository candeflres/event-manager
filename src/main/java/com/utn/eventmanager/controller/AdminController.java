package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.user.EmployeeCreateRequest;
import com.utn.eventmanager.dto.user.UserResponse;
import com.utn.eventmanager.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    //----------------------------------------//
    //----------- CREATE EMPLOYEE -----------//
    //--------------------------------------//
    @PostMapping
    public UserResponse createEmployee(
            @RequestBody @Valid EmployeeCreateRequest request) {
        return userService.createEmployee(request);
    }
}
