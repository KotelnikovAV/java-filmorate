package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseGenreDto {
    @NotNull
    private int id;
    @NotNull
    private String name;
}
