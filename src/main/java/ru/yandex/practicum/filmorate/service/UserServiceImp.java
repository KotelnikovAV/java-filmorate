package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inmemory.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserServiceImp implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImp(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User addFriend(int id, int friendId) {
        log.info("Начало процесса добавления друга");
        log.debug("Значения переменных при добавлении друга id и friendId: " + id + ", " + friendId);

        if (id == friendId) {
            log.info("Ошибка: значения id и friendId при добавлении в друзья совпадают");
            throw new ValidationException("Вы не можете добавить самого себя в друзья.");
        }

        List<Integer> friends = userStorage.getUser(id).getFriends();
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);

        if (friends.contains(friendId)) {
            log.info("Данный пользователь уже есть в друзьях");
            throw new DuplicatedDataException(friend.getName() + " уже есть у вас в друзьях.");
        }

        friends.add(friendId);
        friend.getFriends().add(id);
        log.info("Друг добавлен");
        return user;
    }

    @Override
    public User deleteFriend(int id, int friendId) {
        log.info("Начало процесса удаления друга");
        log.debug("Значения переменных при удалении друга id и friendId: " + id + ", " + friendId);

        if (id == friendId) {
            log.info("Ошибка: значения id и friendId при удалении совпадают");
            throw new ValidationException("Вы не можете удалить самого себя из друзей.");
        }

        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        List<Integer> userFriends = user.getFriends();
        List<Integer> friendFriends = friend.getFriends();

        if (!userFriends.contains(friendId)) {
            log.info("Данный пользователь отсутствует в друзьях");
            throw new NotFoundException(friend.getName() + " отсутствует у вас в друзьях.");
        }

        userFriends.remove(friendId);
        friendFriends.remove(id);
        log.info("Друг удален");
        return user;
    }

    @Override
    public List<User> getMutualFriends(int id, int otherId) {
        log.info("Начало процесса получения списка общих друзей");
        log.debug("Значения переменных при получении списка общих друзей id и otherId: " + id + ", " + otherId);

        if (id == otherId) {
            log.info("Ошибка: значения id и otherId при поиске общих друзей совпадают");
            throw new ValidationException("Вы не можете искать общих друзей с самим собой.");
        }

        List<Integer> userFriends = userStorage.getUser(id).getFriends();
        User otherUser = userStorage.getUser(otherId);
        List<Integer> otherUserFriends = otherUser.getFriends();
        List<Integer> mutualFriends = userFriends.stream().filter(otherUserFriends::contains).toList();

        if (mutualFriends.isEmpty()) {
            log.info("У пользователей нет общих друзей");
            throw new NotFoundException("У вас с пользователем " + otherUser.getName() + " нет общих друзей.");
        }

        log.info("Список общих друзей получен");
        return mutualFriends.stream().map(userStorage::getUser).toList();
    }

    @Override
    public List<User> getAllFriends(int id) {
        log.info("Начало процесса получения списка всех друзей");
        log.debug("Значения переменной при получении списка всех друзей id: " + id);
        List<Integer> friends = userStorage.getUser(id).getFriends();
        log.info("Список всех друзей получен");
        return friends.stream().map(userStorage::getUser).toList();
    }

    @Override
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    @Override
    public User getUser(int id) {
       return userStorage.getUser(id);
    }
}