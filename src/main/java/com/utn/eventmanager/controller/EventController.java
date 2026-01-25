package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.event.EventCreateRequest;
import com.utn.eventmanager.dto.event.EventResponse;
import com.utn.eventmanager.dto.event.EventUpdateRequest;
import com.utn.eventmanager.dto.event.EventUpdateStatusRequest;
import com.utn.eventmanager.service.event.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // ======================
    //CLIENTE
    // ======================

    //-------------------------------------//
    //----------- CREATE EVENT -----------//
    //------------------------------------//
    @PostMapping
    public EventResponse createEvent(
            Authentication authentication,
            @RequestBody @Valid EventCreateRequest request
    ) {
        return eventService.createEvent(authentication, request);
    }

    //-------------------------------------------------------------------//
    //----------- UPDATE EVENT (solo si el estado lo permite -----------//
    //-----------------------------------------------------------------//
    @PutMapping("/{eventId}")
    public EventResponse updateEvent(@PathVariable Long eventId,
                                     @RequestBody @Valid EventUpdateRequest request, Authentication authentication) {
        return eventService.updateEvent(eventId, request, authentication);
    }

    //------------------------------------------------//
    //----------- SUBMIT EVENT FOR REVIEW -----------//
    //----------------------------------------------//
    @PutMapping("/{eventId}/send-review")
    public EventResponse sendToReview(@PathVariable Long eventId) {
        return eventService.sendToReview(eventId);
    }

    //---------------------------------------------//
    //----------- LIST CUSTOMER EVENTS -----------//
    //-------------------------------------------//
    @GetMapping
    public Page<EventResponse> getEventsByUser(Authentication authentication, @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "9") int size) {
        return eventService.getEventsByUser(authentication, page, size);
    }

    //------------------------------------------//
    //----------- SEE EVENT DETAILS -----------//
    //----------------------------------------//
    @GetMapping("/detail/{eventId}")
    public EventResponse getEventDetail(@PathVariable Long eventId) {
        return eventService.getEventDetail(eventId);
    }

    //----------------------------------------------------------------------------------------------------------------//
    // ======================
    // EMPLEADO
    // ======================

    //--------------------------------------------//
    //----------- CHANGE EVENT STATUS -----------//
    //------------------------------------------//
    //Change event status
    @PutMapping("/{eventId}/status")
    public EventResponse updateEventStatus(@PathVariable Long eventId,
                                           @RequestBody @Valid EventUpdateStatusRequest request, Authentication authentication) {
        return eventService.updateEventStatus(eventId, request, authentication);
    }
}