package com.utn.eventmanager.model;

import com.utn.eventmanager.model.enums.AuditAction;
import com.utn.eventmanager.model.enums.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AuditAction action;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private AuditEntity entity;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Size(max = 255)
    @Column(length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
