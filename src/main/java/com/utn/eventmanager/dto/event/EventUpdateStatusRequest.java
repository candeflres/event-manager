package com.utn.eventmanager.dto.event;

import com.utn.eventmanager.model.enums.EventStatus;
import jakarta.validation.constraints.NotNull;
// este dto es para actualziar COMO EMPLEADO
public class EventUpdateStatusRequest {

    @NotNull
    private EventStatus status;

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}