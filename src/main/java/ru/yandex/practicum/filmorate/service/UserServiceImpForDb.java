package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.indb.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@Primary
public class UserServiceImpForDb implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpForDb(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addFriend(int id, int friendId) {
        log.info("Начало процесса добавления друга");
        log.debug("Значения переменных при добавлении друга id и friendId: " + id + ", " + friendId);

        if (id == friendId) {
            log.info("Ошибка: значения id и friendId при добавлении в друзья совпадают");
            throw new ValidationException("Вы не можете добавить самого себя в друзья.");
        }

        return userRepository.addFriend(id, friendId);
    }

    @Override
    public User deleteFriend(int id, int friendId) {
        log.info("Начало процесса удаления друга");
        log.debug("Значения переменных при удалении друга id и friendId: " + id + ", " + friendId);

        if (id == friendId) {
            log.info("Ошибка: значения id и friendId при удалении совпадают");
            throw new ValidationException("Вы не можете удалить самого себя из друзей.");
        }

        return userRepository.deleteFriend(id, friendId);
    }

    @Override
    public List<User> getMutualFriends(int id, int otherId) {
        log.info("Начало процесса получения списка общих друзей");
        log.debug("Значения переменных при получении списка общих друзей id и otherId: " + id + ", " + otherId);

        if (id == otherId) {
            log.info("Ошибка: значения id и otherId при поиске общих друзей совпадают");
            throw new ValidationException("Вы не можете искать общих друзей с самим собой.");
        }

        List<User> userFriends = userRepository.findFriends(id);
        List<Integer> otherUserFriends = userRepository.findFriends(otherId)
                .stream()
                .map(User::getId)
                .toList();
        List<User> mutualFriends = userFriends
                .stream()
                .filter(user -> otherUserFriends.contains(user.getId()))
                .toList();

        if (mutualFriends.isEmpty()) {
            log.info("У пользователей нет общих друзей");
            throw new NotFoundException("У вас с данным пользователем нет общих друзей.");
        }

        return mutualFriends;
    }

    @Override
    public List<User> getAllFriends(int id) {
        log.info("Начало процесса получения списка всех друзей");
        log.debug("Значения переменной при получении списка всех друзей id: " + id);
        return userRepository.findFriends(id);
    }

    @Override
    public Collection<User> findAll() {
        log.info("Начало процесса получения списка всех пользователей");
        return userRepository.findAll();
    }

    @Override
    public User create(User user) {
        log.info("Начало процесса создания пользователя");

        if ((user.getName() == null) || (user.getName().isBlank())) {
            user.setName(user.getLogin());
        }
        return userRepository.create(user);
    }

    @Override
    public User update(User newUser) {
        log.info("Начало процесса обновления пользователя");

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        return userRepository.update(newUser);
    }

    @Override
    public User getUser(int id) {
        log.info("Начало процесса получения пользователя с id = " + id);
        return userRepository.getUser(id);
    }
}