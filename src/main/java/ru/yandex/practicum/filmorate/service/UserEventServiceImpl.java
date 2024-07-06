package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserEventDto;
import ru.yandex.practicum.filmorate.mapper.UserEventMapper;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.UserEventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventServiceImpl implements UserEventService {
    UserEventRepository userEventRepository;

    @Override
    public List<UserEventDto> getAllUserEvents(int userId) {
        log.info("Начало процесса получения UserEvent");
        List<UserEvent> userEvents = userEventRepository.getAllUserEvents(userId);
        log.info("Преобразование UserEvent в UserEventDto ");
        return userEvents.stream()
                .map(UserEventMapper::mapToUserEventDto)
                .toList();
    }
}
