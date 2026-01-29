package com.utn.eventmanager.service.audit;

import com.utn.eventmanager.dto.audit.AuditLogResponse;
import com.utn.eventmanager.model.AuditLog;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.AuditAction;
import com.utn.eventmanager.model.enums.AuditEntity;

import java.util.List;

public interface AuditLogService {

    void log(
            AuditAction action,
            AuditEntity entity,
            String description,
            User user
    );

    public void log(
            AuditAction action,
            AuditEntity entity,
            String description,
            User user,
            Long eventId
    );
    List<AuditLogResponse> getFiltered(
            AuditEntity entity,
            Long userId,
            String userEmail,
            Long eventId,
            String order
    );

    List<AuditLog> getAll();
}