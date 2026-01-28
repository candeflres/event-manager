package com.utn.eventmanager.service.element;

import com.utn.eventmanager.dto.element.ElementCreateRequest;
import com.utn.eventmanager.dto.element.ElementResponse;
import com.utn.eventmanager.dto.element.ElementUpdateRequest;
import org.springframework.security.core.Authentication;

import java.util.List;
// interfaz para implementacion
public interface ElementService {

    ElementResponse findByIdForManagement(Long id);
    ElementResponse create (ElementCreateRequest request, Authentication authentication);
    ElementResponse update (Long id, ElementUpdateRequest request, Authentication authentication);
    List<ElementResponse> findAll();
    List<ElementResponse> findAvailable();
    ElementResponse findById(Long id);
}
