package com.utn.eventmanager.service.event;

import com.utn.eventmanager.dto.event.EventCreateRequest;
import com.utn.eventmanager.dto.event.EventResponse;
import com.utn.eventmanager.dto.event.EventUpdateRequest;
import com.utn.eventmanager.dto.event.EventUpdateStatusRequest;

import java.util.List;

public interface EventService {

    // ======================
    // CLIENT
    // ======================
    EventResponse createEvent(Long userId, EventCreateRequest request);

    EventResponse updateEvent(Long eventId, EventUpdateRequest request);

    EventResponse sendToReview(Long eventId);

    List<EventResponse> getEventsByUser(Long userId);

    EventResponse getEventDetail(Long eventId);

    // ======================
    // EMPLOYEE
    // ======================
    List<EventResponse> getAllEvents();

    EventResponse updateEventStatus(Long eventId, EventUpdateStatusRequest request);
}