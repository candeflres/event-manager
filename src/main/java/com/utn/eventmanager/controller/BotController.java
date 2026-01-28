package com.utn.eventmanager.controller;


import com.utn.eventmanager.dto.bot.BotActionRequest;
import com.utn.eventmanager.dto.bot.BotResponseDTO;
import com.utn.eventmanager.service.bot.BotService;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/action")
    public BotResponseDTO handleAction(@RequestBody BotActionRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Usuario intentando acción: " + (auth != null ? auth.getName() : "ANÓNIMO"));
        boolean isLogged = auth != null && auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken);

        return switch (request.getAction()) {

            case "ABOUT" -> botService.about();
            case "REGISTER" -> botService.registerInfo();
            case "LOGIN" -> botService.loginInfo();
            case "RECOVER_PASSWORD" -> botService.recoverPasswordInfo();

            case "CREATE_EVENT" -> botService.createEvent();
            case "MY_EVENTS" -> botService.myEvents();
            case "AVAILABLE_DATES" -> botService.availableDates();
            case "CHECK_DATE" -> botService.checkDate(request.getValue());

            case "BACK" -> botService.startBot();

            default -> botService.startBot();
        };
    }
}


