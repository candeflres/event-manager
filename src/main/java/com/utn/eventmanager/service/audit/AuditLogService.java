package com.utn.eventmanager.service.audit;

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

    List<AuditLog> getAll();
}