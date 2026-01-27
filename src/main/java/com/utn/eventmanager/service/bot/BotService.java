package com.utn.eventmanager.service.bot;

import com.utn.eventmanager.dto.bot.BotOptionDTO;
import com.utn.eventmanager.dto.bot.BotResponseDTO;
import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.enums.EventStatus;
import com.utn.eventmanager.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BotService {

    private final EventRepository eventRepository;

    public BotService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // ==========================
    // BOT PUBLICO
    // ==========================

    public BotResponseDTO startBot() {
        return new BotResponseDTO(
                "¡Hola! 👋 Soy el asistente de EventManager. ¿En qué puedo ayudarte?",
                List.of(
                        new BotOptionDTO(1, "Conocer sobre nosotros", "ABOUT"),
                        new BotOptionDTO(2, "Cómo registrarme", "REGISTER"),
                        new BotOptionDTO(3, "Cómo iniciar sesión", "LOGIN"),
                        new BotOptionDTO(4, "Recuperar contraseña", "RECOVER_PASSWORD")
                )
        );
    }

    public BotResponseDTO about() {
        return new BotResponseDTO(
                "EventManager es una plataforma para crear y gestionar eventos fácilmente 🎉",
                backOption()
        );
    }

    public BotResponseDTO registerInfo() {
        return new BotResponseDTO(
                "Podés registrarte desde la opción 'Registrarse' completando tus datos.",
                backOption()
        );
    }

    public BotResponseDTO loginInfo() {
        return new BotResponseDTO(
                "Ingresá tu email y contraseña para iniciar sesión.",
                backOption()
        );
    }

    public BotResponseDTO recoverPasswordInfo() {
        return new BotResponseDTO(
                "Usá la opción 'Olvidé mi contraseña' para recuperarla.",
                backOption()
        );
    }

    // ==========================
    // BOT LOGUEADO
    // ==========================

    public BotResponseDTO loggedBot() {
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

    public BotResponseDTO createEvent() {
        return new BotResponseDTO(
                "Para crear un evento, entrá a 'Mis Eventos' y hacé clic en 'Crear evento'.",
                backOption()
        );
    }

    public BotResponseDTO myEvents() {
        return new BotResponseDTO(
                "En 'Mis Eventos' podés ver todos tus eventos y su estado.",
                backOption()
        );
    }

    public BotResponseDTO availableDates() {

        List<Event> events = eventRepository.findByStatusIn(
                List.of(EventStatus.APPROVED, EventStatus.PENDING)
        );

        if (events.isEmpty()) {
            return new BotResponseDTO(
                    "No hay eventos registrados. Todas las fechas están disponibles 📅",
                    backOption()
            );
        }

        String fechasOcupadas = events.stream()
                .map(event -> event.getEventDate().toString())
                .distinct()
                .sorted()
                .collect(Collectors.joining("\n📅 "));

        return new BotResponseDTO(
                "Estas fechas ya están ocupadas:\n📅 " + fechasOcupadas,
                backOption()
        );
    }

    public BotResponseDTO checkDate(String date) {

        LocalDate selectedDate = LocalDate.parse(date);

        boolean occupied = eventRepository.existsByEventDateAndStatusIn(
                selectedDate,
                List.of(EventStatus.APPROVED, EventStatus.PENDING)
        );

        return new BotResponseDTO(
                occupied
                        ? "La fecha " + date + " ya está ocupada ❌"
                        : "La fecha " + date + " está disponible ✅",
                backOption()
        );
    }

    // ==========================
    // OPCION COMUN
    // ==========================

    private List<BotOptionDTO> backOption() {
        return List.of(
                new BotOptionDTO(0, "Volver al inicio", "BACK")
        );
    }
}
