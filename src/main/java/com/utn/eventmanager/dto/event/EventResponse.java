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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public EventStatus getStatus() {
        return status;
    }

    public BigDecimal getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public void setEstimatedBudget(BigDecimal estimatedBudget) {
        this.estimatedBudget = estimatedBudget;
    }
}
