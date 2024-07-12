package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.UserEventDto;

import java.util.List;

public interface UserEventService {

    List<UserEventDto> getAllUserEvents(int userId);
}
