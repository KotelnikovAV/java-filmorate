package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.annotation.Nonnegative;

@Data
@Builder
public class Review {

    private int reviewId;

    @NotBlank
    private String content;

    @NotNull
    private Boolean isPositive;

    @NotNull
    @Nonnegative
    private Integer userId;

    @NotNull
    @Nonnegative
    private Integer filmId;

    private int useful;
}