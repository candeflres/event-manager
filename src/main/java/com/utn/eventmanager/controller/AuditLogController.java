package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.audit.AuditLogResponse;
import com.utn.eventmanager.model.AuditLog;
import com.utn.eventmanager.model.enums.AuditEntity;
import com.utn.eventmanager.service.audit.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
    @GetMapping
    public Page<AuditLogResponse> getAuditLogs(
            @RequestParam(required = false) AuditEntity entity,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) Long eventId,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return auditLogService.getFiltered(
                entity,
                userEmail,
                eventId,
                order,
                page,
                size
        );
    }
}