package com.utn.eventmanager.service.element;

import com.utn.eventmanager.dto.element.ElementCreateRequest;
import com.utn.eventmanager.dto.element.ElementResponse;
import com.utn.eventmanager.dto.option.OptionResponse;
import com.utn.eventmanager.model.Option;
import com.utn.eventmanager.repository.ElementRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.utn.eventmanager.model.Element;
import com.utn.eventmanager.dto.element.ElementUpdateRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class ElementServiceImpl implements ElementService {

    private final ElementRepository elementRepository;

    public ElementServiceImpl(ElementRepository elementRepository) {
        this.elementRepository = elementRepository;
    }

    //--------------------------------------//
    //----------- CREATE METHOD -----------//
    //------------------------------------//
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

    @Override
    public ElementResponse findByIdForManagement(Long id) {
        Element element = elementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Elemento no encontrado"
                ));

        ElementResponse dto = new ElementResponse();
        dto.setId(element.getId());
        dto.setName(element.getName());
        dto.setDescription(element.getDescription());
        dto.setAvailable(element.getAvailable());

        List<OptionResponse> optionResponses =
                element.getOptions().stream()
                        .map(option -> {
                            OptionResponse or = new OptionResponse();
                            or.setId(option.getId());
                            or.setName(option.getName());
                            or.setDescription(option.getDescription());
                            or.setPrice(option.getPrice());
                            or.setAvailable(option.getAvailable());
                            or.setElementId(element.getId());
                            return or;
                        })
                        .toList();

        dto.setOptions(optionResponses);
        return dto;
    }
    //--------------------------------------//
    //----------- UPDATE METHOD -----------//
    //------------------------------------//
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

    //------------------------------------------------------------------------------//
    //----------- LIST ALL ELEMENTS METHOD (only for admin / employees) -----------//
    //----------------------------------------------------------------------------//
    @Override
    public List<ElementResponse> findAll() {
        return elementRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //------------------------------------------------------------------------------//
    //----------- LIST ALL AVAILABLE ELEMENTS METHOD (only for clients) -----------//
    //----------------------------------------------------------------------------//
    @Override
    public List<ElementResponse> findAvailable() {
        return elementRepository.findByAvailableTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //---------------------------------------------------//
    //----------- MAPPER FROM ELEMENT TO DTO -----------//
    //-------------------------------------------------//
    private ElementResponse toResponse(Element element) {

        ElementResponse dto = new ElementResponse();
        dto.setId(element.getId());
        dto.setName(element.getName());
        dto.setDescription(element.getDescription());
        dto.setAvailable(element.getAvailable());

        List<OptionResponse> optionResponses =
                element.getOptions().stream()
                        .filter(Option::getAvailable)
                        .map(option -> {
                            OptionResponse or = new OptionResponse();
                            or.setId(option.getId());
                            or.setName(option.getName());
                            or.setDescription(option.getDescription());
                            or.setPrice(option.getPrice());
                            or.setAvailable(option.getAvailable());
                            or.setElementId(element.getId());
                            return or;
                        })
                        .toList();

        dto.setOptions(optionResponses);

        return dto;
    }

    public ElementResponse findById(Long id) {
        Element element = elementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Elemento no encontrado"
                ));

        if (!element.getAvailable()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Elemento no disponible"
            );
        }

        return toResponse(element);
    }

}
