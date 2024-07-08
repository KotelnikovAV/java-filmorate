package ru.yandex.practicum.filmorate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserEventDto {
    private int eventId;
    private int userId;
    private int entityId;
    private String eventType;
    private String operation;
    private LocalDate timestamp;
}
