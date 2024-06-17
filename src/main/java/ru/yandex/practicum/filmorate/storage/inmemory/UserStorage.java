package ru.yandex.practicum.filmorate.storage.inmemory;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    User getUser(int id);
}