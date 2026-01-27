package com.utn.eventmanager.dto.bot;

public class BotOptionDTO {
    private int option;
    private String text;
    private String action;

    public BotOptionDTO(int option, String text, String action) {
        this.option = option;
        this.text = text;
        this.action = action;
    }

    // getters

    public int getOption() {
        return option;
    }

    public String getText() {
        return text;
    }

    public String getAction() {
        return action;
    }
}