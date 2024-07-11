package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEventDto {
    @Positive
    private int eventId;
    @Positive
    private int userId;
    @Positive
    private int entityId;
    @NotBlank
    private String eventType;
    @NotBlank
    private String operation;
    @NotNull
    @Positive
    private long timestamp;
}
