package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import javax.annotation.Nonnegative;

@Data
@Builder
public class ReviewDto {
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
