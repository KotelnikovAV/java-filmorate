package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
public class ReviewDto {

    private Integer reviewId;

    @NotBlank
    private String content;

    @NotNull
    private Boolean isPositive;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer filmId;

    private int useful;
}
