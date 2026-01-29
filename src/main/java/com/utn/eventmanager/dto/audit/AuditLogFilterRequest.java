package com.utn.eventmanager.dto.audit;

import com.utn.eventmanager.model.enums.AuditEntity;

public class AuditLogFilterRequest {

    private AuditEntity entity;

    private Long userId;
    private String userEmail;

    private Long eventId;

    private String order = "desc";

}