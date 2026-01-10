package com.utn.eventmanager.dto.element;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// este DTO se utiliza para crear un request
// no posee available ya que por defecto es TRUE

public class ElementCreateRequest {
    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 255)
    private String description;
}