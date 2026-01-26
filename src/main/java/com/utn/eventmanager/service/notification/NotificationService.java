package com.utn.eventmanager.service.notification;

import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.User;
import com.utn.eventmanager.model.enums.EventStatus;
import com.utn.eventmanager.model.enums.UserRole;
import com.utn.eventmanager.repository.UserRepository;
import com.utn.eventmanager.service.passwordReset.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public NotificationService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public void notifyEventStatusChange(
            Event event,
            EventStatus oldStatus,
            EventStatus newStatus
    ) {

        User client = event.getUser();

        if (client.getRole() == UserRole.CLIENT) {
            notifyClient(client, event, oldStatus, newStatus);
        }

        notifyEmployeeIfNeeded(event, oldStatus, newStatus);
    }

    public void notifyEventCompleted(Event event) {
        if (event.getUser() == null) return;

        emailService.sendEventCompleted(
                event.getUser(),
                event
        );
    }

    private void notifyClient(
            User client,
            Event event,
            EventStatus oldStatus,
            EventStatus newStatus
    ) {
        switch (newStatus) {
            case PENDING -> emailService.sendEventPending(client, event);
            case APPROVED -> emailService.sendEventApproved(client, event);
            case REJECTED -> emailService.sendEventRejected(client, event);
            case COMPLETED -> emailService.sendEventCompleted(client, event);
            default -> {}
        }
    }

    private void notifyEmployeeIfNeeded(
            Event event,
            EventStatus oldStatus,
            EventStatus newStatus
    ) {
        boolean isNewEvent = oldStatus == null && newStatus == EventStatus.PENDING;
        boolean isUpdatedByClient =
                oldStatus == EventStatus.REJECTED && newStatus == EventStatus.PENDING;

        if (!isNewEvent && !isUpdatedByClient) return;

        List<User> employees = userRepository.findByRole(UserRole.EMPLOYEE);

        for (User employee : employees) {
            if (isNewEvent) {
                emailService.sendNewEventToEmployee(employee, event);
            } else {
                emailService.sendUpdatedEventToEmployee(employee, event);
            }
        }
    }
}