package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User addFriend(long id, long friendId);

    User deleteFriend(long id, long friendId);

    List<User> getMutualFriends(long id, long otherId);

    List<User> getAllFriends(long id);
}