package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.option.OptionCreateRequest;
import com.utn.eventmanager.dto.option.OptionResponse;
import com.utn.eventmanager.dto.option.OptionUpdateRequest;
import com.utn.eventmanager.service.option.OptionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    //--------------------------------------------//
    //----------- CREATE (employee) -------------//
    //------------------------------------------//
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public OptionResponse create(@RequestBody OptionCreateRequest request, Authentication authentication) {
        return optionService.create(request, authentication);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public void delete(Authentication authentication ,@PathVariable Long id) {
        optionService.delete(authentication ,id);
    }
    //--------------------------------------------//
    //----------- UPDATE (employee) -------------//
    //------------------------------------------//
    @PutMapping("/{id}")
    public OptionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody OptionUpdateRequest request, Authentication authentication) {
        return optionService.update(id, request, authentication);
    }

    //--------------------------------------------//
    //----------- LIST ALL (employee) -----------//
    //------------------------------------------//
    @GetMapping
    public List<OptionResponse> findAll() {
        return optionService.findAll();
    }

    //-------------------------------------------------//
    //----------- LIST AVAILABLE (client) -----------//
    //-----------------------------------------------//
    @GetMapping("/available")
    public List<OptionResponse> findAvailable() {
        return optionService.findAvailable();
    }

    //------------------------------------------------//
    //----------- LIST OPTIONS BY ELEMENT -----------//
    //----------------------------------------------//
    @GetMapping("/element/{elementId}")
    public List<OptionResponse> findByElement(
            @PathVariable Long elementId) {
        return optionService.findByElement(elementId);
    }
}