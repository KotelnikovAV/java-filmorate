package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEventDto {
    @NotNull
    @Positive
    private int eventId;
    @NotNull
    @Positive
    private int userId;
    @NotNull
    @Positive
    private int entityId;
    @NotNull
    @NotBlank
    private String eventType;
    @NotNull
    @NotBlank
    private String operation;
    @NotNull
    @Positive
    private long timestamp;
}
