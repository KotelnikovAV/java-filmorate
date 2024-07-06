package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserEvent {
    private int eventId;
    private int userId;
    private int entityId;
    private EventType eventType;
    private Operation operation;
    private Instant timestamp;
}
