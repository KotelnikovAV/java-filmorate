package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserEventDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserEventMapper;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.UserEventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventServiceImpl implements UserEventService {
    private final UserEventRepository userEventRepository;

    @Override
    public List<UserEventDto> getAllUserEvents(int userId) {
        log.info("Начало процесса получения UserEvent по userId:{}", userId);
        List<UserEvent> userEvents = userEventRepository.getAllUserEvents(userId);
        log.info("UserEvent успешно получены");

        List<UserEventDto> listUserEventsDto =  userEvents.stream()
                .map(UserEventMapper::mapToUserEventDto)
                .toList();

        if (listUserEventsDto.isEmpty()) {
            throw new NotFoundException("Список событий пуст");
        }
        return listUserEventsDto;
    }
}
