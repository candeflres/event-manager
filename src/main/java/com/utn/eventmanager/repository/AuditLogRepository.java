package com.utn.eventmanager.repository;

import com.utn.eventmanager.model.AuditLog;
import com.utn.eventmanager.model.enums.AuditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByOrderByTimestampDesc();

    @Query("""
    SELECT a FROM AuditLog a
    WHERE (:entity IS NULL OR a.entity = :entity)
      AND (:userEmail IS NULL OR LOWER(a.user.email) LIKE LOWER(CONCAT('%', :userEmail, '%')))
      AND (:eventId IS NULL OR a.eventId = :eventId)
""")
    Page<AuditLog> findFiltered(
            @Param("entity") AuditEntity entity,
            @Param("userEmail") String userEmail,
            @Param("eventId") Long eventId,
            Pageable pageable
    );

}