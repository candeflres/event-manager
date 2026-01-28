package com.utn.eventmanager.service.option;

import com.utn.eventmanager.dto.option.OptionCreateRequest;
import com.utn.eventmanager.dto.option.OptionResponse;
import com.utn.eventmanager.dto.option.OptionUpdateRequest;
import com.utn.eventmanager.model.Element;
import com.utn.eventmanager.model.Option;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.AuditAction;
import com.utn.eventmanager.model.enums.AuditEntity;
import com.utn.eventmanager.repository.ElementRepository;
import com.utn.eventmanager.repository.OptionRepository;
import com.utn.eventmanager.repository.UserRepository;
import com.utn.eventmanager.service.audit.AuditLogService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final ElementRepository elementRepository;
    private final AuditLogService auditLogService;
    private final UserRepository userRepository;

    public OptionServiceImpl(OptionRepository optionRepository,
                             ElementRepository elementRepository, AuditLogService auditLogService, UserRepository userRepository) {
        this.optionRepository = optionRepository;
        this.elementRepository = elementRepository;
        this.auditLogService = auditLogService;
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public void delete(Authentication authentication, Long id) {

        User user = userRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opción no encontrada"));

        option.setAvailable(false);
        optionRepository.save(option);

        auditLogService.log(
                AuditAction.DELETE,
                AuditEntity.OPTION,
                "El usuario dio de baja la opción: " + option.getName(),
                user
        );
    }
    //--------------------------------------//
    //----------- CREATE METHOD -----------//
    //------------------------------------//
    @Override
    @Transactional
    public OptionResponse create(OptionCreateRequest request, Authentication authentication) {

        User user = userRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        Element element = elementRepository.findById(request.getElementId())
                .orElseThrow(() -> new IllegalArgumentException("Element not found"));

        if (optionRepository.existsByNameIgnoreCaseAndElementId(
                request.getName(), element.getId())) {
            throw new IllegalStateException(
                    "Ya existe una opción con ese nombre para este elemento"
            );
        }

        Option option = new Option();
        option.setName(request.getName());
        option.setDescription(request.getDescription());
        option.setPrice(request.getPrice());
        option.setAvailable(true);
        option.setElement(element);

        Option saved = optionRepository.save(option);
        auditLogService.log(
                AuditAction.CREATE,
                AuditEntity.OPTION,
                "El usuario creó la opción: " + option.getName(),
                user
        );

        return toResponse(saved);
    }

    //--------------------------------------//
    //----------- UPDATE METHOD -----------//
    //------------------------------------//
    @Override
    public OptionResponse update(Long id, OptionUpdateRequest request, Authentication authentication) {

        User user = userRepository
                .findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        option.setName(request.getName());
        option.setDescription(request.getDescription());
        option.setPrice(request.getPrice());
        option.setAvailable(request.getAvailable());

        Option updated = optionRepository.save(option);
        auditLogService.log(
                AuditAction.UPDATE,
                AuditEntity.OPTION,
                "El usuario actualizó la opción: " + option.getName(),
                user
        );
        return toResponse(updated);
    }

    //---------------------------------------------------------------------//
    //----------- FIND ALL METHOD ( admins and employees only) -----------//
    //-------------------------------------------------------------------//
    @Override
    public List<OptionResponse> findAll() {
        return optionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //------------------------------------------------------------//
    //----------- FIND ALL AVAILABLE METHOD (clients) -----------//
    //----------------------------------------------------------//
    @Override
    public List<OptionResponse> findAvailable() {
        return optionRepository.findByAvailableTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //-----------------------------------//
    //----------- FIND BY ID -----------//
    //---------------------------------//
    @Override
    public List<OptionResponse> findByElement(Long elementId) {
        return optionRepository.findByElementId(elementId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    //-------------------------------------------------//
    //----------- MAPPER FROM MODEL TO DTO -----------//
    //-----------------------------------------------//
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