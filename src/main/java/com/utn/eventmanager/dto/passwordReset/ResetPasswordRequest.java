package com.utn.eventmanager.dto.passwordReset;

public record ResetPasswordRequest(String email, String code, String newPassword) {
}
