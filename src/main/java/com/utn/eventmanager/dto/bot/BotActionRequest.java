package com.utn.eventmanager.dto.bot;

public class BotActionRequest {
    private String action; // Ej: AVAILABLE_DATES, CHECK_DATE
    private String value;  // Ej: fecha "2026-02-15" (opcional)

    public BotActionRequest() {
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
