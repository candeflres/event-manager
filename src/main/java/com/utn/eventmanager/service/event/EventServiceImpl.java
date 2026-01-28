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
import com.utn.eventmanager.service.notification.NotificationService;
import com.utn.eventmanager.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final OptionRepository optionRepository;
    private final EventOptionRepository eventOptionRepository;
    @Autowired
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    public EventServiceImpl(
            EventRepository eventRepository,
            UserRepository userRepository,
            UserService userService,
            OptionRepository optionRepository,
            EventOptionRepository eventOptionRepository,
            NotificationService notificationService
    ) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.optionRepository = optionRepository;
        this.eventOptionRepository = eventOptionRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public void cancelEvent(Long eventId, Authentication authentication) {

        User user = userService.getUserFromAuth(authentication);
        Event event = findEvent(eventId);

        if (!event.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No sos dueño de este evento"
            );
        }

        if (event.getStatus() == EventStatus.COMPLETED ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede cancelar un evento completado"
            );
        }

        event.setStatus(EventStatus.CANCELLED);
        eventRepository.save(event);
    }

    // ======================
    // CLIENT
    // ======================

    @Override
    @Transactional
    public EventResponse createEvent(
            Authentication authentication,
            EventCreateRequest request
    ) {

        User user = userService.getUserFromAuth(authentication);

        if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.EMPLOYEE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Los empleados y administradores no pueden crear eventos"
            );
        }

        LocalDate minDate = LocalDate.now().plusDays(2);

        if (request.getEventDate().isBefore(minDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El evento debe crearse con al menos 2 días de anticipación"
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

        notificationService.notifyEventStatusChange(
                savedEvent,
                null,
                EventStatus.PENDING
        );

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

        if (user.getRole() != UserRole.CLIENT ) {
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

        if (
                event.getStatus() != EventStatus.PENDING &&
                        event.getStatus() != EventStatus.REJECTED
        ) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "El evento solo puede editarse si está PENDIENTE o RECHAZADO"
            );
        }

        EventStatus oldStatus = event.getStatus();

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

        if (oldStatus == EventStatus.REJECTED) {
            event.setStatus(EventStatus.PENDING);
        }

        Event savedEvent = eventRepository.save(event);

        if (oldStatus == EventStatus.REJECTED) {
            notificationService.notifyEventStatusChange(
                    savedEvent,
                    oldStatus,
                    EventStatus.PENDING
            );
        }

        return mapToResponse(savedEvent);
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


    @Transactional
    public void updatePastEventsToCompleted() {

        List<Event> events = eventRepository
                .findByEventDateBeforeAndStatusIn(
                        LocalDate.now(),
                        List.of(EventStatus.PENDING, EventStatus.APPROVED)
                );

        for (Event event : events) {
            event.setStatus(EventStatus.COMPLETED);

            notificationService.notifyEventCompleted(event);
        }

        eventRepository.saveAll(events);
    }


    @Override
    public Page<EventResponse> getEventsByUser(Authentication authentication, int page, int size) {
        User user = userService.getUserFromAuth(authentication);

        Page<Event> eventos;

        Pageable pageable = PageRequest.of(page, size);

        if (user.getRole() == UserRole.EMPLOYEE) {
            eventos = eventRepository.findByStatusNot(EventStatus.CANCELLED, pageable);
        } else {
            eventos = eventRepository.findByUserIdAndStatusNot(
                    user.getId(),
                    EventStatus.CANCELLED,
                    pageable
            );
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

        EventStatus oldStatus = event.getStatus();

        validateStatusTransition(oldStatus, request.getStatus());

        event.setStatus(request.getStatus());

        Event savedEvent = eventRepository.save(event);

        notificationService.notifyEventStatusChange(
                savedEvent,
                oldStatus,
                request.getStatus()
        );

        return mapToResponse(savedEvent);
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

    private void validateStatusTransition(EventStatus current, EventStatus next) {

        if (current != EventStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se pueden aprobar o rechazar eventos pendientes"
            );
        }

        if (next != EventStatus.APPROVED && next != EventStatus.REJECTED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Estado inválido"
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

    @Override
    public Page<EventResponse> getFilteredEvents(
            Authentication authentication,
            EventStatus status,
            String order,
            int page,
            int size
    ) {
        User user = userService.getUserFromAuth(authentication);

        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by("eventDate").descending()
                : Sort.by("eventDate").ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Event> events;

        if (user.getRole() == UserRole.EMPLOYEE) {

            if (status != null) {
                events = eventRepository.findByStatusNotAndStatus(
                        EventStatus.CANCELLED,
                        status,
                        pageable
                );
            } else {
                events = eventRepository.findByStatusNot(
                        EventStatus.CANCELLED,
                        pageable
                );
            }

        } else { // CLIENT

            if (status != null) {
                events = eventRepository.findByUserIdAndStatusNotAndStatus(
                        user.getId(),
                        EventStatus.CANCELLED,
                        status,
                        pageable
                );
            } else {
                events = eventRepository.findByUserIdAndStatusNot(
                        user.getId(),
                        EventStatus.CANCELLED,
                        pageable
                );
            }
        }

        return events.map(this::mapToResponse);
    }



}