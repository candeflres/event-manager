package com.utn.eventmanager.scheduler;

import com.utn.eventmanager.service.event.EventServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventStatusScheduler {

    private final EventServiceImpl eventService;

    public EventStatusScheduler(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void updatePastEvents() {
        eventService.updatePastEventsToCompleted();
    }
}