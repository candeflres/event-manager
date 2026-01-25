package com.utn.eventmanager.service.event;

import com.utn.eventmanager.dto.event.EventCreateRequest;
import com.utn.eventmanager.dto.event.EventResponse;
import com.utn.eventmanager.dto.event.EventUpdateRequest;
import com.utn.eventmanager.dto.event.EventUpdateStatusRequest;
import com.utn.eventmanager.dto.option.OptionResponse;
import com.utn.eventmanager.dto.user.UserResponse;
import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.EventOption;
import com.utn.eventmanager.model.Option;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.EventStatus;
import com.utn.eventmanager.model.enums.UserRole;
import com.utn.eventmanager.repository.EventOptionRepository;
import com.utn.eventmanager.repository.EventRepository;
import com.utn.eventmanager.repository.OptionRepository;
import com.utn.eventmanager.repository.UserRepository;
import com.utn.eventmanager.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final OptionRepository optionRepository;
    private final EventOptionRepository eventOptionRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public EventServiceImpl(
            EventRepository eventRepository,
            UserRepository userRepository,
            UserService userService,
            OptionRepository optionRepository,
            EventOptionRepository eventOptionRepository
    ) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.optionRepository = optionRepository;
        this.eventOptionRepository = eventOptionRepository;
    }

    // ======================
    // CLIENT
    // ======================

    @Override
    @Transactional
    public EventResponse createEvent(Authentication authentication,
                                     EventCreateRequest request) {

        User user = userService.getUserFromAuth(authentication);

        if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.EMPLOYEE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Los empleados y administradores no pueden crear eventos"
            );
        }

        Event event = new Event();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setStatus(EventStatus.PENDING);
        event.setEstimatedBudget(BigDecimal.ZERO);
        event.setUser(user);

        List<Option> options = optionRepository.findAllById(request.getOptionIds());

        if (options.size() != request.getOptionIds().size()) {
            throw new IllegalArgumentException("Una o más opciones no existen");
        }

        for (Option option : options) {
            EventOption eo = new EventOption();
            eo.setEvent(event);
            eo.setOption(option);

            event.getOptions().add(eo);
        }

        event.setEstimatedBudget(
                calculateEstimatedBudget(event.getOptions())
        );

        Event savedEvent = eventRepository.save(event);

        return mapToResponse(savedEvent);
    }

    @Override
    public EventResponse updateEvent(
            Long eventId,
            EventUpdateRequest request,
            Authentication authentication
    ) {
        User user = userService.getUserFromAuth(authentication);
        Event event = findEvent(eventId);

        if (user.getRole() != UserRole.CLIENT) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo los clientes pueden editar eventos"
            );
        }

        if (!event.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No sos dueño de este evento"
            );
        }

        if (!isEditable(event)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "El evento solo puede editarse mientras esté en estado PENDIENTE"
            );
        }

        if (request.getName() != null) {
            event.setName(request.getName());
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }

        if (request.getOptionIds() != null) {

            event.getOptions().clear();

            List<Option> options =
                    optionRepository.findAllById(request.getOptionIds());

            if (options.size() != request.getOptionIds().size()) {
                throw new IllegalArgumentException("Una o más opciones no existen");
            }

            for (Option option : options) {
                EventOption eo = new EventOption();
                eo.setEvent(event);
                eo.setOption(option);
                event.getOptions().add(eo);
            }

            event.setEstimatedBudget(
                    calculateEstimatedBudget(event.getOptions())
            );
        }

        return mapToResponse(eventRepository.save(event));
    }

    @Override
    public EventResponse sendToReview(Long eventId) {
        Event event = findEvent(eventId);

        if (event.getStatus() != EventStatus.PENDING) {
            throw new IllegalStateException(
                    "Solo los eventos en borrador pueden enviarse a revisión"
            );
        }

        event.setStatus(EventStatus.PENDING);
        return mapToResponse(eventRepository.save(event));
    }

    @Override
    public Page<EventResponse> getEventsByUser(Authentication authentication, int page, int size) {
        User user = userService.getUserFromAuth(authentication);

        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventos;

        if (user.getRole() == UserRole.EMPLOYEE) {
            eventos = eventRepository.findAll(pageable);
        } else {
            eventos = eventRepository.findByUserId(user.getId(), pageable);
        }

        return eventos.map(this::mapToResponse);
    }

    @Override
    public EventResponse getEventDetail(Long eventId) {
        return mapToResponse(findEvent(eventId));
    }

    @Override
    public EventResponse getPublicEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        return mapToResponse(event);
    }



    // ======================
    // EMPLOYEE
    // ======================
    @Override
    public EventResponse updateEventStatus(
            Long eventId,
            EventUpdateStatusRequest request,
            Authentication authentication
    ) {
        User user = userService.getUserFromAuth(authentication);
        Event event = findEvent(eventId);

        if (user.getRole() != UserRole.EMPLOYEE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo empleados pueden aprobar o rechazar eventos"
            );
        }

        validateStatusTransition(event.getStatus(), request.getStatus());

        event.setStatus(request.getStatus());

        return mapToResponse(eventRepository.save(event));
    }

    // ======================
    // PRIVATE METHODS
    // ======================

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
    }

    private boolean isEditable(Event event) {
        return event.getStatus() == EventStatus.PENDING;
    }

    private void validateStatusTransition(EventStatus current,
                                          EventStatus next) {

        if (current == EventStatus.APPROVED || current == EventStatus.CANCELLED) {
            throw new IllegalStateException(
                    "No se puede modificar un evento finalizado"
            );
        }
    }

    private BigDecimal calculateEstimatedBudget(List<EventOption> eventOptions) {
        return eventOptions.stream()
                .map(eo -> eo.getOption().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private EventResponse mapToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setName(event.getName());
        response.setDescription(event.getDescription());
        response.setEventDate(event.getEventDate());
        response.setStatus(event.getStatus());
        response.setEstimatedBudget(event.getEstimatedBudget());

        List<OptionResponse> optionResponses =
                event.getOptions().stream()
                        .map(eo -> {
                            Option o = eo.getOption();
                            OptionResponse r = new OptionResponse();
                            r.setId(o.getId());
                            r.setName(o.getName());
                            r.setPrice(o.getPrice());
                            return r;
                        })
                        .toList();

        response.setOptions(optionResponses);
        return response;
    }
}