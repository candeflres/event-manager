package com.utn.eventmanager.repository;

import com.utn.eventmanager.model.AuditLog;
import com.utn.eventmanager.model.enums.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByOrderByTimestampDesc();

    List<AuditLog> findByEntityOrderByTimestampDesc(AuditEntity entity);

    List<AuditLog> findByUserIdOrderByTimestampDesc(Long userId);


}