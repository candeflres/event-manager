package com.utn.eventmanager.dto.passwordReset;

public record VerifyCodeRequest(String email, String code) {}
