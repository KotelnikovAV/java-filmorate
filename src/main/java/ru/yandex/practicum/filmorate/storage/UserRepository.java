package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    User create(User user);

    User update(User newUser);

    Optional<User> getUser(int id);

    void delete(int id);
}