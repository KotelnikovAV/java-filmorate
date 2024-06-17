package ru.yandex.practicum.filmorate.storage.indb;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    User getUser(int id);

    List<User> findFriends(int id);

    User addFriend(int id, int friendId);

    User deleteFriend(int id, int friendId);
}