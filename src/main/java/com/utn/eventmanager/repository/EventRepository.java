package com.utn.eventmanager.repository;

import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    // CLIENTE
    // listar eventos de un cliente
    List<Event> findByUserId(Long userId);
    // filtrar eventos del cliente por estado
    List<Event> findByUserIdAndStatus(Long userId, EventStatus status);
    // Ordenar por fecha
    List<Event> findByUserIdOrderByEventDateAsc(Long userId);
    List<Event> findByUserIdOrderByEventDateDesc(Long userId);

    // EMPLEADO
    // ver todos los eventos
    List<Event> findAll();

    // filtrar por estado
    List<Event> findByStatus(EventStatus status);

    // filtrar por rango de fechas
    List<Event> findByEventDateBetween(LocalDate start, LocalDate end);

    // verificaciones
    // verificacion de si está en estado X (x ej para ver si está en PENDING el cliente puede editarlo todavia, una vez q está aprobado no)
    boolean existsByIdAndStatusIn(Long id, List<EventStatus> statuses);

}