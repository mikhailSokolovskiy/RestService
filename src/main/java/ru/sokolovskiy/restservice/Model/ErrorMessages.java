package ru.sokolovskiy.restservice.Model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorMessages {

    EMPTY(""),
    VALIDATION("Ошибка валидации"),
    UNKNOWN("Не поддерживаемая ошибка");

    private final String description;

    ErrorMessages(String description) {
        this.description = description;
    }

    @JsonValue
    public String getName() {
        return description;
    }

}
