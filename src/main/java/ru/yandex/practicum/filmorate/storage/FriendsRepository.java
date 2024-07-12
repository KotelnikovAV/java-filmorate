package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsRepository {

    List<User> getAllFriendsById(int id);

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);
}
