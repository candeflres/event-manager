package com.utn.eventmanager.service.event;

import com.utn.eventmanager.dto.event.EventCreateRequest;
import com.utn.eventmanager.dto.event.EventResponse;
import com.utn.eventmanager.dto.event.EventUpdateRequest;
import com.utn.eventmanager.dto.event.EventUpdateStatusRequest;
import com.utn.eventmanager.model.EventOption;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

public interface EventService {

    // ======================
    // CLIENT
    // ======================
    EventResponse createEvent(Authentication authentication, EventCreateRequest request);

    EventResponse updateEvent(Long eventId, EventUpdateRequest request);

    EventResponse sendToReview(Long eventId);

    Page<EventResponse> getEventsByUser(Authentication authentication, int page, int size);

    EventResponse getEventDetail(Long eventId);
    // ======================
    // EMPLOYEE
    // ======================

    EventResponse updateEventStatus(Long eventId, EventUpdateStatusRequest request);
}