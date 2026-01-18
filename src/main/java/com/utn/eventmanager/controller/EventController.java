package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.event.EventCreateRequest;
import com.utn.eventmanager.dto.event.EventResponse;
import com.utn.eventmanager.dto.event.EventUpdateRequest;
import com.utn.eventmanager.dto.event.EventUpdateStatusRequest;
import com.utn.eventmanager.service.event.EventService;
import jakarta.validation.Valid;
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
    @PostMapping("/user/{userId}")
    public EventResponse createEvent(@PathVariable Long userId,
                                     @RequestBody @Valid EventCreateRequest request) {
        return eventService.createEvent(userId, request);
    }

    //-------------------------------------------------------------------//
    //----------- UPDATE EVENT (solo si el estado lo permite -----------//
    //-----------------------------------------------------------------//
    @PutMapping("/{eventId}")
    public EventResponse updateEvent(@PathVariable Long eventId,
                                     @RequestBody @Valid EventUpdateRequest request) {
        return eventService.updateEvent(eventId, request);
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
    @GetMapping("/user/{userId}")
    public List<EventResponse> getEventsByUser(@PathVariable Long userId) {
        return eventService.getEventsByUser(userId);
    }

    //------------------------------------------//
    //----------- SEE EVENT DETAILS -----------//
    //----------------------------------------//
    @GetMapping("/{eventId}")
    public EventResponse getEventDetail(@PathVariable Long eventId) {
        return eventService.getEventDetail(eventId);
    }

    //----------------------------------------------------------------------------------------------------------------//
    // ======================
    // EMPLEADO
    // ======================

    //------------------------------------------//
    //----------- SEE ALL EVENTS -----------//
    //----------------------------------------//
    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    //--------------------------------------------//
    //----------- CHANGE EVENT STATUS -----------//
    //------------------------------------------//
    //Change event status
    @PutMapping("/{eventId}/status")
    public EventResponse updateEventStatus(@PathVariable Long eventId,
                                           @RequestBody @Valid EventUpdateStatusRequest request) {
        return eventService.updateEventStatus(eventId, request);
    }
}