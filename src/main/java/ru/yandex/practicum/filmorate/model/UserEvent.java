package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserEvent {
    private int eventId;
    @Positive
    private int userId;
    @Positive
    private int entityId;
    @NotNull
    private EventType eventType;
    @NotNull
    private Operation operation;
    @NotNull
    @Positive
    private Timestamp timestamp;
}
