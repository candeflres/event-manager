package com.utn.eventmanager.repository;

import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // =================
    // CLIENT
    // =================

    Page<Event> findByUserId(Long userId, Pageable pageable);

    Page<Event> findByUserIdAndStatusNot(
            Long userId,
            EventStatus status,
            Pageable pageable
    );

    Page<Event> findByUserIdAndStatusNotAndStatus(
            Long userId,
            EventStatus excluded,
            EventStatus status,
            Pageable pageable
    );

    List<Event> findByEventDateBeforeAndStatusIn(
            LocalDate date,
            List<EventStatus> statuses
    );

    // =================
    // EMPLOYEE
    // =================
    Page<Event> findByStatusNot(
            EventStatus status,
            Pageable pageable
    );

    Page<Event> findByStatusNotAndStatus(
            EventStatus excluded,
            EventStatus status,
            Pageable pageable
    );
    Page<Event> findByStatus(EventStatus status, Pageable pageable);

    Page<Event> findByEventDateBetween(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    // =================
    // VERIFICATIONS
    // =================

    boolean existsByIdAndStatusIn(Long id, List<EventStatus> statuses);

    // verifica eventos por fecha y estado
    boolean existsByEventDateAndStatusIn(
            LocalDate eventDate,
            List<EventStatus> statuses
    );

    // Trae todas las fechas ocupadas
    List<Event> findByStatusIn(List<EventStatus> statuses);
    long count();
    long countByStatus(EventStatus status);


    boolean existsByEventDateAndStatus(LocalDate eventDate, EventStatus status);
}