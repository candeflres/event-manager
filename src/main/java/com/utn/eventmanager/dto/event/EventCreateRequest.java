package com.utn.eventmanager.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @PositiveOrZero
    private BigDecimal estimatedBudget;
}