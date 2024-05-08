package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserManager {
    private final Map<Integer, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        log.info("Начало процесса добавления пользователя");

        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }

        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    public User update(User newUser) {
        log.info("Начало процесса обновления пользователя");

        if (newUser.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }

        if (newUser.getName() == null) {
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

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .max(Comparator.comparingInt(id -> id))
                .orElse(0);
        return ++currentMaxId;
    }
}
