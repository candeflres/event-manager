package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.passwordReset.ForgotPasswordRequest;
import com.utn.eventmanager.dto.passwordReset.ResetPasswordRequest;
import com.utn.eventmanager.service.passwordReset.PasswordResetService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }

    @PostMapping("/forgot-password")
    public void forgot(@RequestBody ForgotPasswordRequest request) {
        service.sendCode(request.email());
    }

    @PostMapping("/reset-password")
    public void reset(@RequestBody ResetPasswordRequest request) {
        service.resetPassword(request);
    }
}