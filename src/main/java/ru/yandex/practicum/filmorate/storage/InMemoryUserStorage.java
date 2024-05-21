package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        log.info("Начало процесса добавления пользователя");

        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        user.setFriends(new ArrayList<>());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("Начало процесса обновления пользователя");

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        if (!users.containsKey(newUser.getId())) {
            log.error("При обновлении не был найден пользователь");
            throw new NotFoundException("Пользователя с id = " + newUser.getId() + " не существует");
        }

        User oldUser = users.get(newUser.getId());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setEmail(newUser.getEmail());
        log.info("Пользователь обновлен");
        return oldUser;
    }

    @Override
    public User getUser(long id) {
        if (!users.containsKey(id)) {
            log.info("Пользователя с таким id не существует");
            throw new NotFoundException("Пользователя с id = " + id + " не существует");
        }
        return users.get(id);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .max(Comparator.comparingLong(id -> id))
                .orElse(0L);
        return ++currentMaxId;
    }
}