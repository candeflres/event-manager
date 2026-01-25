package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.event.EventResponse;
import com.utn.eventmanager.service.event.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/events")
public class PublicEventController {
    private final EventService eventService;

    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public EventResponse getPublicEvent(@PathVariable Long id) {
        return eventService.getPublicEvent(id);
    }
}
