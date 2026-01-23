package com.utn.eventmanager.repository;

import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    //=================
    // CLIENT
    //=================

    //-----------------------------------------------//
    //----------- LIST A CLIENT'S EVENTS -----------//
    //---------------------------------------------//
    Page<Event> findByUserId(Long userId, Pageable pageable);

    //------------------------------------------------------//
    //----------- FILTER CLIENT EVENTS BY STATE -----------//
    //----------------------------------------------------//
    List<Event> findByUserIdAndStatus(Long userId, EventStatus status);

    //-------------------------------------//
    //----------- SORT BY DATE -----------//
    //-----------------------------------//
    // Sort by date
    List<Event> findByUserIdOrderByEventDateAsc(Long userId);
    List<Event> findByUserIdOrderByEventDateDesc(Long userId);

//====================================================================================================================//

    //=================
    // EMPLOYEE
    //=================

    //--------------------------------------//
    //----------- SEE ALL EVENTS-----------//
    //------------------------------------//
    List<Event> findAll();

    //-----------------------------------------//
    //----------- FILTER BY STATUS -----------//
    //---------------------------------------//
    List<Event> findByStatus(EventStatus status);

    //---------------------------------------------//
    //----------- FILTER BY DATE RANGE -----------//
    //-------------------------------------------//
    List<Event> findByEventDateBetween(LocalDate start, LocalDate end);

    //--------------------------------------//
    //----------- VERIFICATIONS -----------//
    //------------------------------------//
    // verificacion de si está en estado X (x ej para ver si está en PENDING
    // el cliente puede editarlo todavia, una vez q está aprobado no)
    boolean existsByIdAndStatusIn(Long id, List<EventStatus> statuses);

}