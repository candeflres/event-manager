package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.option.OptionCreateRequest;
import com.utn.eventmanager.dto.option.OptionResponse;
import com.utn.eventmanager.dto.option.OptionUpdateRequest;
import com.utn.eventmanager.service.option.OptionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    // create (EMPLOYEE)
    @PostMapping
    public OptionResponse create(
            @Valid @RequestBody OptionCreateRequest request) {
        return optionService.create(request);
    }

    // update (EMPLOYEE)
    @PutMapping("/{id}")
    public OptionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody OptionUpdateRequest request) {
        return optionService.update(id, request);
    }

    // list all (employee)
    @GetMapping
    public List<OptionResponse> findAll() {
        return optionService.findAll();
    }

    // LIST AVAILABLE (client)
    @GetMapping("/available")
    public List<OptionResponse> findAvailable() {
        return optionService.findAvailable();
    }

    // list options by element
    @GetMapping("/element/{elementId}")
    public List<OptionResponse> findByElement(
            @PathVariable Long elementId) {
        return optionService.findByElement(elementId);
    }
}