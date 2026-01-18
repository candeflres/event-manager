package com.utn.eventmanager.service.event;

import com.utn.eventmanager.dto.event.EventCreateRequest;
import com.utn.eventmanager.dto.event.EventResponse;
import com.utn.eventmanager.dto.event.EventUpdateRequest;
import com.utn.eventmanager.dto.event.EventUpdateStatusRequest;
import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.EventStatus;
import com.utn.eventmanager.repository.EventRepository;
import com.utn.eventmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventServiceImpl(EventRepository eventRepository,
                            UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    // ======================
    // CLIENT
    // ======================

    @Override
    public EventResponse createEvent(Long userId, EventCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Event event = new Event();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setStatus(EventStatus.PENDING);
        event.setEstimatedBudget(BigDecimal.ZERO);
        event.setUser(user);

        return mapToResponse(eventRepository.save(event));
    }

    @Override
    public EventResponse updateEvent(Long eventId, EventUpdateRequest request) {
        Event event = findEvent(eventId);

        if (!isEditable(event)) {
            throw new IllegalStateException(
                    "El evento no puede modificarse en el estado actual"
            );
        }

        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());

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
    public List<EventResponse> getEventsByUser(Long userId) {
        return eventRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public EventResponse getEventDetail(Long eventId) {
        return mapToResponse(findEvent(eventId));
    }

    // ======================
    // EMPLOYEE
    // ======================

    @Override
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public EventResponse updateEventStatus(Long eventId,
                                           EventUpdateStatusRequest request) {
        Event event = findEvent(eventId);

        validateStatusTransition(event.getStatus(), request.getStatus());

        event.setStatus(request.getStatus());

        // 🔜 Acá después enchufás AuditLog
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

    private EventResponse mapToResponse(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setName(event.getName());
        response.setDescription(event.getDescription());
        response.setEventDate(event.getEventDate());
        response.setStatus(event.getStatus());
        response.setEstimatedBudget(event.getEstimatedBudget());
        return response;
    }
}