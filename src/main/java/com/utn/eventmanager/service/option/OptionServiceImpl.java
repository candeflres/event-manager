package com.utn.eventmanager.service.option;

import com.utn.eventmanager.dto.option.OptionCreateRequest;
import com.utn.eventmanager.dto.option.OptionResponse;
import com.utn.eventmanager.dto.option.OptionUpdateRequest;
import com.utn.eventmanager.model.Element;
import com.utn.eventmanager.model.Option;
import com.utn.eventmanager.repository.ElementRepository;
import com.utn.eventmanager.repository.OptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final ElementRepository elementRepository;

    public OptionServiceImpl(OptionRepository optionRepository,
                             ElementRepository elementRepository) {
        this.optionRepository = optionRepository;
        this.elementRepository = elementRepository;
    }

    // create method
    @Override
    public OptionResponse create(OptionCreateRequest request) {

        if (optionRepository.existsByNameIgnoreCaseAndElementId(
                request.getName(), request.getElementId())) {
            throw new RuntimeException("Option already exists for this element");
        }

        Element element = elementRepository.findById(request.getElementId())
                .orElseThrow(() -> new RuntimeException("Element not found"));

        Option option = new Option();
        option.setName(request.getName());
        option.setDescription(request.getDescription());
        option.setPrice(request.getPrice());
        option.setAvailable(true);
        option.setElement(element);

        Option saved = optionRepository.save(option);

        return toResponse(saved);
    }

    // update method
    @Override
    public OptionResponse update(Long id, OptionUpdateRequest request) {

        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        option.setName(request.getName());
        option.setDescription(request.getDescription());
        option.setPrice(request.getPrice());
        option.setAvailable(request.getAvailable());

        Option updated = optionRepository.save(option);

        return toResponse(updated);
    }

    // find all method (admins and employees only)
    @Override
    public List<OptionResponse> findAll() {
        return optionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //find all available method (clients)
    @Override
    public List<OptionResponse> findAvailable() {
        return optionRepository.findByAvailableTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }
    // find by id
    @Override
    public List<OptionResponse> findByElement(Long elementId) {
        return optionRepository.findByElementId(elementId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // mapper from model to dto
    private OptionResponse toResponse(Option option) {
        OptionResponse dto = new OptionResponse();
        dto.setId(option.getId());
        dto.setName(option.getName());
        dto.setDescription(option.getDescription());
        dto.setPrice(option.getPrice());
        dto.setAvailable(option.getAvailable());
        dto.setElementId(option.getElement().getId());
        return dto;
    }
}