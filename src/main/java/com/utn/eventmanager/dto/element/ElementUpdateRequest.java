package com.utn.eventmanager.dto.element;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// este DTO se utiliza para actualizar un request
// contiene available que se utilizará para la baja logica

public class ElementUpdateRequest {

    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private Boolean available;
}