package com.utn.eventmanager.controller;

import com.utn.eventmanager.dto.bot.BotActionRequest;
import com.utn.eventmanager.dto.bot.BotOptionDTO;
import com.utn.eventmanager.dto.bot.BotResponseDTO;
import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.enums.EventStatus;
import com.utn.eventmanager.repository.EventRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bot")
@CrossOrigin(origins = "http://localhost:4200")
public class BotController {

    private final EventRepository eventRepository;

    public BotController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // ==========================
    // BOT PUBLICO (SIN SESION)
    // ==========================

    @GetMapping("/start")
    public BotResponseDTO startBot(){
        return new BotResponseDTO(
                "¡Hola! 👋 Soy el asistente de EventManager. ¿En qué puedo ayudarte?",
                List.of(
                        new BotOptionDTO(1, "Conocer sobre nosotros", "ABOUT"),
                        new BotOptionDTO(2, "Cómo registrarme", "REGISTER"),
                        new BotOptionDTO(3, "Cómo iniciar sesión", "LOGIN"),
                        new BotOptionDTO(4, "Recuperar contraseña", "RECOVER_PASSWORD")
                  )
        );
    };

    // ==========================
    // BOT LOGUEADO
    // ==========================
    @GetMapping("/logged")
    public BotResponseDTO loggedBot(){
        return new BotResponseDTO(
                "¡Bienvenido! ¿Qué te gustaría hacer?",
                List.of(
                        new BotOptionDTO(1, "Crear un evento", "CREATE_EVENT"),
                        new BotOptionDTO(2, "Ver estado de mis eventos", "MY_EVENTS"),
                        new BotOptionDTO(3, "Ver fechas disponibles", "AVAILABLE_DATES"),
                        new BotOptionDTO(4, "Verificar fecha específica", "CHECK_DATE")
                )
        );
    }

    // ==========================
    // ACCIONES DEL BOT
    // ==========================
    @PostMapping("/action")
    public BotResponseDTO handleAction(@RequestBody BotActionRequest request) {

        return switch (request.getAction()) {

            // -------- PUBLICO --------
            case "ABOUT" -> about();
            case "REGISTER" -> registerInfo();
            case "LOGIN" -> loginInfo();
            case "RECOVER_PASSWORD" -> recoverPasswordInfo();

            // -------- LOGUEADO --------
            case "CREATE_EVENT" -> createEvent();
            case "MY_EVENTS" -> myEvents();
            case "AVAILABLE_DATES" -> availableDates();
            case "CHECK_DATE" -> checkDate(request.getValue());

            default -> new BotResponseDTO(
                    "Por favor ingresá una opción válida 🤖",
                    List.of(
                            new BotOptionDTO(0, "Volver al inicio", "BACK")
                    )
            );
        };
    }

    // ================
    // MÉTODOS DEL BOT
    // ================
    private BotResponseDTO about() {
        return new BotResponseDTO(
                "EventManager es una plataforma para crear y gestionar eventos fácilmente 🎉",
                List.of()
        );
    }

    private BotResponseDTO registerInfo() {
        return new BotResponseDTO(
                "Podés registrarte desde la opción 'Registrarse' completando tus datos.",
                List.of()
        );
    }

    private BotResponseDTO loginInfo() {
        return new BotResponseDTO(
                "Ingresá tu email y contraseña para iniciar sesión.",
                List.of()
        );
    }

    private BotResponseDTO recoverPasswordInfo() {
        return new BotResponseDTO(
                "Usá la opción 'Olvidé mi contraseña' para recuperarla.",
                List.of()
        );
    }

    private BotResponseDTO createEvent() {
        return new BotResponseDTO(
                "Para crear un evento, entrá a 'Mis Eventos' y hacé clic en 'Crear evento'.",
                List.of()
        );
    }

    private BotResponseDTO myEvents() {
        return new BotResponseDTO(
                "En 'Mis Eventos' podés ver todos tus eventos y su estado.",
                List.of()
        );
    }

    private BotResponseDTO availableDates() {

        List<Event> events = eventRepository.findByStatusIn(
                List.of(EventStatus.APPROVED, EventStatus.PENDING)
        );

        if (events.isEmpty()) {
            return new BotResponseDTO(
                    "No hay eventos registrados. Todas las fechas están disponibles",
                    List.of()
            );
        }

        String fechasOcupadas = events.stream()
                .map(event -> event.getEventDate().toString())
                .distinct()
                .sorted()
                .collect(Collectors.joining("\n📅 "));

        return new BotResponseDTO(
                "Estas fechas ya están ocupadas:\n📅 " + fechasOcupadas,
                List.of()
        );
    }

    private BotResponseDTO checkDate(String date) {

        LocalDate selectedDate = LocalDate.parse(date);

        List<Event> events = eventRepository.findByEventDateAndStatusIn(
                selectedDate,
                List.of(EventStatus.APPROVED, EventStatus.PENDING)
        );

        if (events.isEmpty()) {
            return new BotResponseDTO(
                    "La fecha " + date + " está disponible.",
                    List.of()
            );
        }

        return new BotResponseDTO(
                "La fecha " + date + " ya está ocupada.",
                List.of()
        );
    }
}



