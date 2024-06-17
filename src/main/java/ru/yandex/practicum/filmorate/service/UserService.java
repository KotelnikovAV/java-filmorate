package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    User addFriend(int id, int friendId);

    User deleteFriend(int id, int friendId);

    List<User> getMutualFriends(int id, int otherId);

    List<User> getAllFriends(int id);

    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    User getUser(int id);
}