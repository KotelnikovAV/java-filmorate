package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.UserEvent;

import java.util.List;

public interface UserEventRepository {

    UserEvent createUserEvent(UserEvent userEvent);

    List<UserEvent> getAllUserEvents(int userId);
}
