package com.utn.eventmanager.service.bot;

import com.utn.eventmanager.dto.bot.BotOptionDTO;
import com.utn.eventmanager.dto.bot.BotResponseDTO;
import com.utn.eventmanager.model.Event;
import com.utn.eventmanager.model.enums.EventStatus;
import com.utn.eventmanager.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
                        new BotOptionDTO(1, "Quiero saber más", "ABOUT"),
                        new BotOptionDTO(2, "¿Cómo me registro?", "REGISTER"),
                        new BotOptionDTO(3, "¿Cómo inicio sesión?", "LOGIN"),
                        new BotOptionDTO(4, "Recuperar contraseña", "RECOVER_PASSWORD"),
                        new BotOptionDTO(5,"¡Contactate con nosotros!","WHATSAPP"),
                        new BotOptionDTO(6, "Borré mi cuenta, ¿como la recupero?", "RECOVER_ACCOUNT")
                )
        );
    }

    public BotResponseDTO about() {
        return new BotResponseDTO(
                "Somos una plataforma que permite que crees el evento de tus sueños. Te ofrecemos opciones que se adaptan a tus gustos y presupuesto, con precios claros desde el inicio.",
                backOption()
        );
    }

    public BotResponseDTO recoverAccount() {
        return new BotResponseDTO(
                "Si borraste tu cuenta y necesitás reactivarla, escribinos por WhatsApp para que podamos ayudarte.",
                backOption()
        );
    }

    public BotResponseDTO registerInfo() {
        return new BotResponseDTO(
                "Podés registrarte desde la opción 'Registrarse' completando tus datos. No te preocupes, siempre priorizamos la seguridad de nuestros clientes.",
                backOption()
        );
    }

    public BotResponseDTO loginInfo() {
        return new BotResponseDTO(
                "Podés iniciar sesión desde la opción 'Iniciar sesión' completando tu correo y tu contraseña. ",
                backOption()
        );
    }

    public BotResponseDTO recoverPasswordInfo() {
        return new BotResponseDTO(
                "¡No te preocupes! Podés recuperarla desde recuperar contraseña, te enviaremos un código para que puedas reingresar.",
                backOption()
        );
    }

    public BotResponseDTO whatsapp() {
        String numero = "5492235243997";
        String mensaje = "Quiero contactarme con un asesor.";
        String url = "https://wa.me/" + numero + "?text=" + mensaje.replace(" ", "%20");

        return new BotResponseDTO(
                "¡Claro! Podés contactarnos haciendo clic aquí: " +
                        "<a href='" + url + "' target='_blank' style='color: #25D366; font-weight: bold;'>Chatear por WhatsApp 💬</a>",
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
                        new BotOptionDTO(3, "Verificar fecha específica", "CHECK_DATE"),
                        new BotOptionDTO(4, "¡Contactate con nosotros!", "WHATSAPP")
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

    public BotResponseDTO checkDate(String date) {

        LocalDate selectedDate;

        try {
            selectedDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return new BotResponseDTO(
                    "La fecha ingresada no es válida ❌\n" +
                            "Probá nuevamente (YYYY-MM-DD)",
                    backOption(),
                    "WAITING_DATE"
            );
        }

        LocalDate today = LocalDate.now();

        if (selectedDate.isBefore(today)) {
            return new BotResponseDTO(
                    "La fecha " + selectedDate + " ya pasó 📆\n" +
                            "Elegí una fecha futura.",
                    backOption(),
                    "WAITING_DATE"
            );
        }

        LocalDate minAllowedDate = today.plusDays(2);

        if (selectedDate.isBefore(minAllowedDate)) {
            return new BotResponseDTO(
                    "Los eventos deben crearse con al menos 48 hs de anticipación ⏳\n" +
                            "Probá con una fecha a partir del " + minAllowedDate,
                    backOption(),
                    "WAITING_DATE"
            );
        }

        boolean occupied = eventRepository.existsByEventDateAndStatusIn(
                selectedDate,
                List.of(EventStatus.APPROVED)
        );

        return new BotResponseDTO(
                occupied
                        ? "La fecha " + selectedDate + " ya está ocupada ❌\n" +
                        "Podés probar otra fecha."
                        : "La fecha " + selectedDate + " está disponible ✅\n" +
                        "¿Querés probar otra?",
                backOption(),
                "WAITING_DATE"
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
    public BotResponseDTO askForDate() {
        return new BotResponseDTO(
                "Decime la fecha que querés verificar 📅 (YYYY-MM-DD)",
                backOption(),
                "WAITING_DATE"
        );
    }


}
