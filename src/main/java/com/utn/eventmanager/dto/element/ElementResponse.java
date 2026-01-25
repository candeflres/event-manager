package com.utn.eventmanager.dto.element;

import com.utn.eventmanager.dto.option.OptionResponse;

import java.util.List;

// este DTO se utiliza para respuesta
public class ElementResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<OptionResponse> options;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public List<OptionResponse> getOptions() {
        return options;
    }

    public void setOptions(List<OptionResponse> options) {
        this.options = options;
    }
}