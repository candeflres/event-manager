package com.utn.eventmanager.dto.event;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// este DTO se utiliza para REGISTRAR EVENTOS
// no contiene status porque arranca en PENDING (PENDIENTE)
public class EventCreateRequest {
    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private LocalDate eventDate;

    @NotEmpty
    private List<Long> optionIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public List<Long> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<Long> optionIds) {
        this.optionIds = optionIds;
    }
}