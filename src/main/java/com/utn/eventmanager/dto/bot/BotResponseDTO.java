package com.utn.eventmanager.dto.bot;

import java.util.List;

public class BotResponseDTO {

    private String message;
    private List<BotOptionDTO> options;
    private Object data;

    public BotResponseDTO(String message, List<BotOptionDTO> options) {
        this.message = message;
        this.options = options;
    }

    public BotResponseDTO(String message, List<BotOptionDTO> options, Object data) {
        this.message = message;
        this.options = options;
        this.data = data;
    }

    // getters

    public String getMessage() {
        return message;
    }

    public List<BotOptionDTO> getOptions() {
        return options;
    }

    public Object getData() {
        return data;
    }
}