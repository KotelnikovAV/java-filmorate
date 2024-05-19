package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserServiceImp implements UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public UserServiceImp(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = (InMemoryUserStorage) inMemoryUserStorage;
    }

    @Override
    public User addFriend(long id, long friendId) {
        log.info("Начало процесса добавления друга");
        log.debug("Значения переменных при добавлении друга id и friendId: " + id + ", " + friendId);
        List<Long> friends = inMemoryUserStorage.getUser(id).getFriends();
        User user = inMemoryUserStorage.getUser(id);
        User friend = inMemoryUserStorage.getUser(friendId);

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
    public User deleteFriend(long id, long friendId) {
        log.info("Начало процесса удаления друга");
        log.debug("Значения переменных при удалении друга id и friendId: " + id + ", " + friendId);
        User user = inMemoryUserStorage.getUser(id);
        User friend = inMemoryUserStorage.getUser(friendId);
        List<Long> userFriends = user.getFriends();
        List<Long> friendFriends = friend.getFriends();

        if (!userFriends.contains(friendId)) {
            log.info("Данный пользователь отсутствует в друзьях");
            throw new NotFoundException(friend.getName() + " отсутствует у вас в друзьях.");
        } // тесты в постмане рассчитывают на то, что в этом месте нет исключения, но мне кажется, что оно должно быть

        userFriends.remove(friendId);
        friendFriends.remove(id);
        log.info("Друг удален");
        return user;
    }

    @Override
    public List<User> getMutualFriends(long id, long otherId) {
        log.info("Начало процесса получения списка общих друзей");
        log.debug("Значения переменных при получении списка общих друзей id и otherId: " + id + ", " + otherId);
        List<Long> userFriends = inMemoryUserStorage.getUser(id).getFriends();
        User otherUser = inMemoryUserStorage.getUser(otherId);
        List<Long> otherUserFriends = otherUser.getFriends();
        List<Long> mutualFriends = userFriends.stream().filter(otherUserFriends::contains).toList();

        if (mutualFriends.isEmpty()) {
            log.info("У пользователей нет общих друзей");
            throw new NotFoundException("У вас с пользователем " + otherUser.getName() + " нет общих друзей.");
        }

        log.info("Список общих друзей получен");
        return mutualFriends.stream().map(inMemoryUserStorage::getUser).toList();
    }

    @Override
    public List<User> getAllFriends(long id) {
        log.info("Начало процесса получения списка всех друзей");
        log.debug("Значения переменной при получении списка всех друзей id: " + id);
        List<Long> friends = inMemoryUserStorage.getUser(id).getFriends();
        log.info("Список всех друзей получен");
        return friends.stream().map(inMemoryUserStorage::getUser).toList();
    }
}