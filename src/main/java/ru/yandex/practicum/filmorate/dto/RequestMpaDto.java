package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestMpaDto {
    @NotNull
    private int id;
}
