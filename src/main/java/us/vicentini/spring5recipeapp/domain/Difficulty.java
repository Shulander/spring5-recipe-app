package us.vicentini.spring5recipeapp.domain;

import lombok.Getter;

public enum Difficulty {
    EASY("Easy"),
    MODERATE("Moderate"),
    HARD("Hard");

    @Getter
    private final String text;

    Difficulty(String text) {
        this.text = text;
    }
}
