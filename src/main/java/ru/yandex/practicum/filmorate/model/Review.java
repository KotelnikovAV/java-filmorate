package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class Review {

    private int reviewId;

    @NotBlank
    private String content;

    @NotNull
    private Boolean isPositive;

    @Positive
    private int userId;

    @Positive
    private int filmId;

    private int useful;
}