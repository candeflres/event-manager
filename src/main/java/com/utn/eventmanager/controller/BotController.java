package com.utn.eventmanager.controller;


import com.utn.eventmanager.dto.bot.BotActionRequest;
import com.utn.eventmanager.dto.bot.BotOptionDTO;
import com.utn.eventmanager.dto.bot.BotResponseDTO;
import com.utn.eventmanager.service.bot.BotService;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bot")
@CrossOrigin(origins = "http://localhost:4200")
public class BotController {

    private final BotService botService;

    public BotController(BotService botService) {
        this.botService = botService;
    }

    @GetMapping("/start")
    public BotResponseDTO startBot() {
        return botService.startBot();
    }

    @GetMapping("/logged")
    public BotResponseDTO loggedBot() {
        return botService.loggedBot();
    }
    private BotResponseDTO notLoggedResponse() {
        return new BotResponseDTO(
                "Tenés que iniciar sesión para usar esta opción 🔒",
                List.of(new BotOptionDTO(0, "Volver", "BACK"))
        );
    }

    @PostMapping("/action")
    public BotResponseDTO handleAction(@RequestBody BotActionRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLogged = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);

        return switch (request.getAction()) {

            // ===== PUBLIC =====
            case "ABOUT" -> botService.about();
            case "REGISTER" -> botService.registerInfo();
            case "LOGIN" -> botService.loginInfo();
            case "RECOVER_PASSWORD" -> botService.recoverPasswordInfo();
            case "WHATSAPP" -> botService.whatsapp();

            // ===== LOGGED =====
            case "CREATE_EVENT" -> isLogged
                    ? botService.createEvent()
                    : notLoggedResponse();

            case "MY_EVENTS" -> isLogged
                    ? botService.myEvents()
                    : notLoggedResponse();

            case "CHECK_DATE" -> isLogged
                    ? botService.askForDate()
                    : notLoggedResponse();

            case "WAITING_DATE" -> isLogged
                    ? botService.checkDate(request.getValue())
                    : notLoggedResponse();

            case "BACK" -> isLogged
                    ? botService.loggedBot()
                    : botService.startBot();

            default -> botService.startBot();
        };
    }
}


