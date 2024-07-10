package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.UserEventDto;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.UserEvent;

import java.sql.Timestamp;

@UtilityClass
@Slf4j
public class UserEventMapper {

    public UserEvent mapToUserEvent(UserEventDto userEventDto) {
        log.info("Начало преобразования UserEventDto в UserEvent");

        if (userEventDto == null) {
            return null;
        }

        UserEvent userEvent = UserEvent.builder()
                .eventId(userEventDto.getEventId())
                .userId(userEventDto.getUserId())
                .entityId(userEventDto.getEntityId())
                .eventType(EventType.valueOf(userEventDto.getEventType()))
                .operation(Operation.valueOf(userEventDto.getOperation()))
                .timestamp(new Timestamp(userEventDto.getTimestamp()))
                .build();

        log.info("Преобразование UserEventDto в UserEvent успешно завершено");
        return userEvent;
    }

    public UserEventDto mapToUserEventDto(UserEvent userEvent) {
        log.info("Начало преобразования UserEvent в UserEventDto");

        if (userEvent == null) {
            return null;
        }

        UserEventDto userEventDto = UserEventDto.builder()
                .eventId(userEvent.getEventId())
                .userId(userEvent.getUserId())
                .entityId(userEvent.getEntityId())
                .eventType(userEvent.getEventType().name())
                .operation(userEvent.getOperation().name())
                .timestamp(userEvent.getTimestamp().getTime())
                .build();

        log.info("Преобразование UserEvent в UserEventDto успешно завершено");
        return userEventDto;
    }
}
