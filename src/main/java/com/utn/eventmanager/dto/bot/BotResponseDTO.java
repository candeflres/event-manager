package com.utn.eventmanager.dto.bot;

import java.util.List;

public class BotResponseDTO {

    private String message;
    private List<BotOptionDTO> options;
    private String nextAction; // 👈 estado conversacional

    public BotResponseDTO(String message, List<BotOptionDTO> options) {
        this.message = message;
        this.options = options;
    }

    public BotResponseDTO(String message, List<BotOptionDTO> options, String nextAction) {
        this.message = message;
        this.options = options;
        this.nextAction = nextAction;
    }

    public String getMessage() {
        return message;
    }

    public List<BotOptionDTO> getOptions() {
        return options;
    }

    public String getNextAction() {
        return nextAction;
    }
}