package com.utn.eventmanager.service.passwordReset;

import com.utn.eventmanager.dto.passwordReset.ResetPasswordRequest;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.AuditAction;
import com.utn.eventmanager.model.enums.AuditEntity;
import com.utn.eventmanager.repository.PasswordResetTokenRepository;
import com.utn.eventmanager.repository.UserRepository;
import com.utn.eventmanager.model.PasswordResetToken;
import com.utn.eventmanager.service.audit.AuditLogService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuditLogService auditLogService;

    public PasswordResetService(
            UserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.auditLogService = auditLogService;
    }
    @Transactional
    public void sendCode(String email) {
        User user = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Email no registrado"
                ));

        tokenRepository.deleteByEmail(email);

        String code = String.valueOf((int)(Math.random() * 900000) + 100000);

        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setCode(code);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(token);
        emailService.sendPasswordResetCode(email, code);

    }

    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken token = tokenRepository
                .findByEmailAndCodeAndUsedFalse(request.email(), request.code())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Código inválido"
                ));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Código expirado");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow();

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        token.setUsed(true);
        auditLogService.log(
                AuditAction.UPDATE,
                AuditEntity.USER,
                "Cambió su contraseña mediante recuperación: " + user.getEmail(),
                user
        );
        tokenRepository.save(token);
    }
}
