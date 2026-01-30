package com.utn.eventmanager.service.audit;

import com.sun.java.accessibility.util.EventID;
import com.utn.eventmanager.dto.audit.AuditLogResponse;
import com.utn.eventmanager.model.AuditLog;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.AuditAction;
import com.utn.eventmanager.model.enums.AuditEntity;
import com.utn.eventmanager.repository.AuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void log(
            AuditAction action,
            AuditEntity entity,
            String description,
            User user
    ) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntity(entity);
        log.setDescription(description);
        log.setUser(user);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    @Override
    public void log(
            AuditAction action,
            AuditEntity entity,
            String description,
            User user,
            Long eventId
    ) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntity(entity);
        log.setDescription(description);
        log.setUser(user);
        log.setEventId(eventId);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLog> getAll() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }

    public AuditLogResponse map(AuditLog log) {
        AuditLogResponse res = new AuditLogResponse();
        res.setId(log.getId());
        res.setAction(log.getAction());
        res.setEntity(log.getEntity());
        res.setTimestamp(log.getTimestamp());
        res.setDescription(log.getDescription());
        res.setUserEmail(
                log.getUser() != null ? log.getUser().getEmail() : "SYSTEM"
        );
        res.setEventId(log.getEventId());
        return res;
    }
    public Page<AuditLogResponse> getFiltered(
            AuditEntity entity,
            String userEmail,
            Long eventId,
            String order,
            int page,
            int size
    ) {
        Sort sort = order.equalsIgnoreCase("asc")
                ? Sort.by("timestamp").ascending()
                : Sort.by("timestamp").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AuditLog> logs = auditLogRepository.findFiltered(
                entity,
                userEmail,
                eventId,
                pageable
        );

        return logs.map(this::map);
    }
}