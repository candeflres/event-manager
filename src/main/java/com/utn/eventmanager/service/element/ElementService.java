package com.utn.eventmanager.service.element;

import com.utn.eventmanager.dto.element.ElementCreateRequest;
import com.utn.eventmanager.dto.element.ElementResponse;
import com.utn.eventmanager.dto.element.ElementUpdateRequest;

import java.util.List;
// interfaz para implementacion
public interface ElementService {

    ElementResponse findByIdForManagement(Long id);
    ElementResponse create (ElementCreateRequest request);
    ElementResponse update (Long id, ElementUpdateRequest request);
    List<ElementResponse> findAll();
    List<ElementResponse> findAvailable();
    ElementResponse findById(Long id);
}
