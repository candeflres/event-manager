package com.utn.eventmanager.dto.option;

import java.math.BigDecimal;

// este DTO se utiliza para request
// no expone toda la entidad

public class OptionResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean available;
    private Long elementId;
}
