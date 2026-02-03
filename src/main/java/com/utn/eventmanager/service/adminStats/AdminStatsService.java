package com.utn.eventmanager.service.adminStats;

import com.utn.eventmanager.dto.adminStats.AdminStatsResponse;
import com.utn.eventmanager.model.enums.EventStatus;
import com.utn.eventmanager.model.enums.UserRole;
import com.utn.eventmanager.repository.EventRepository;
import com.utn.eventmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminStatsService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public AdminStatsService(
            EventRepository eventRepository,
            UserRepository userRepository
    ) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public AdminStatsResponse getStats() {

        AdminStatsResponse res = new AdminStatsResponse();

        // eventos
        res.setTotalEvents(eventRepository.count());
        res.setPendingEvents(eventRepository.countByStatus(EventStatus.PENDING));
        res.setApprovedEvents(eventRepository.countByStatus(EventStatus.APPROVED));
        res.setCancelledEvents(eventRepository.countByStatus(EventStatus.CANCELLED));
        res.setCompletedEvents(eventRepository.countByStatus(EventStatus.COMPLETED));

        // usuarios
        res.setTotalUsers(userRepository.count());
        res.setActiveUsers(userRepository.countByActiveTrue());
        res.setInactiveUsers(userRepository.countByActiveFalse());
        res.setClientUsers(userRepository.countByRole(UserRole.CLIENT));
        res.setEmployeeUsers(userRepository.countByRole(UserRole.EMPLOYEE));
        res.setAdminUsers(userRepository.countByRole(UserRole.ADMIN));

        return res;
    }
}