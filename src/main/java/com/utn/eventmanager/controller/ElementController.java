package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.element.ElementCreateRequest;
import com.utn.eventmanager.dto.element.ElementResponse;
import com.utn.eventmanager.dto.element.ElementUpdateRequest;
import com.utn.eventmanager.service.element.ElementService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elements")
public class ElementController {

    private final ElementService elementService;

    public ElementController(ElementService elementService) {
        this.elementService = elementService;
    }

    // create (only employee)
    @PostMapping
    public ElementResponse create(
            @Valid @RequestBody ElementCreateRequest request) {
        return elementService.create(request);
    }

    // update (only employee)
    @PutMapping("/{id}")
    public ElementResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ElementUpdateRequest request) {
        return elementService.update(id, request);
    }

    // list all (employee and admin)
    @GetMapping
    public List<ElementResponse> findAll() {
        return elementService.findAll();
    }

    // list available (client only)
    @GetMapping("/available")
    public List<ElementResponse> findAvailable() {
        return elementService.findAvailable();
    }
}