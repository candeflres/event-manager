package com.utn.eventmanager.service.element;

import com.utn.eventmanager.dto.element.ElementCreateRequest;
import com.utn.eventmanager.dto.element.ElementResponse;
import com.utn.eventmanager.repository.ElementRepository;
import org.springframework.stereotype.Service;
import com.utn.eventmanager.model.Element;
import com.utn.eventmanager.dto.element.ElementUpdateRequest;
import java.util.List;


@Service
public class ElementServiceImpl implements ElementService {

    private final ElementRepository elementRepository;

    public ElementServiceImpl(ElementRepository elementRepository) {
        this.elementRepository = elementRepository;
    }

    // create method
    @Override
    public ElementResponse create(ElementCreateRequest request) {

        // chequea que no exista uno con el mismo nombre
        if (elementRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Element already exists");
        }

        Element element = new Element();
        element.setName(request.getName());
        element.setDescription(request.getDescription());
        element.setAvailable(true);

        Element saved = elementRepository.save(element);

        return toResponse(saved);
    }

    // update method
    @Override
    public ElementResponse update(Long id, ElementUpdateRequest request) {

        // chequea que exista
        Element element = elementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Element not found"));

        element.setName(request.getName());
        element.setDescription(request.getDescription());
        element.setAvailable(request.getAvailable());

        Element updated = elementRepository.save(element);

        return toResponse(updated);
    }

    // list all elements method (only for admin / employees)
    @Override
    public List<ElementResponse> findAll() {
        return elementRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // list all available elements method (only for clients)
    @Override
    public List<ElementResponse> findAvailable() {
        return elementRepository.findByAvailableTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // mapper from element to dto
    private ElementResponse toResponse(Element element) {
        ElementResponse dto = new ElementResponse();
        dto.setId(element.getId());
        dto.setName(element.getName());
        dto.setDescription(element.getDescription());
        dto.setAvailable(element.getAvailable());
        return dto;
    }
}
