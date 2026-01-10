package com.utn.eventmanager.dto.event;

import com.utn.eventmanager.model.enums.EventStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

// este DTO se utiliza para respuestas

public class EventResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate eventDate;
    private EventStatus status;
    private BigDecimal estimatedBudget;
}
