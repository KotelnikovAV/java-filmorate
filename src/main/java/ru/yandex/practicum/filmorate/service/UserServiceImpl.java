package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;
    private final UserEventRepository userEventRepository;
    private final LikesRepository likesRepository;
    private final FilmRepository filmRepository;

    @Override
    public void addFriend(int id, int friendId) {
        log.info("Начало процесса добавления друга");
        log.debug("Значения переменных при добавлении друга id и friendId: {}, {}", id, friendId);

        if (id == friendId) {
            throw new ValidationException("Вы не можете добавить самого себя в друзья.");
        }

        friendsRepository.addFriend(id, friendId);
        log.info("Заявка успешно отправлена");

        log.info("Создание UserEvent добавление в друзья");
        userEventRepository.createUserEvent(UserEvent.builder()
                .userId(id)
                .entityId(friendId)
                .eventType(EventType.FRIEND)
                .operation(Operation.ADD)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build());
    }

    @Override
    public void deleteFriend(int id, int friendId) {
        log.info("Начало процесса удаления друга");
        log.debug("Значения переменных при удалении друга id и friendId: {}, {}", id, friendId);

        if (id == friendId) {
            throw new ValidationException("Вы не можете удалить самого себя из друзей.");
        }

        friendsRepository.deleteFriend(id, friendId);
        log.info("Пользователь удален из друзей");

        log.info("Создание UserEvent удаление из друзей");
        userEventRepository.createUserEvent(UserEvent.builder()
                .userId(id)
                .entityId(friendId)
                .eventType(EventType.FRIEND)
                .operation(Operation.REMOVE)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build());
        log.info("Создание UserEvent удаление из друзей успешно завершено");
    }

    @Override
    public List<UserDto> getMutualFriends(int id, int otherId) {
        log.info("Начало процесса получения списка общих друзей");
        log.debug("Значения переменных при получении списка общих друзей id и otherId: {}, {}", id, otherId);

        if (id == otherId) {
            throw new ValidationException("Вы не можете искать общих друзей с самим собой.");
        }

        List<User> userFriends = friendsRepository.getAllFriendsById(id);
        List<User> otherUserFriends = friendsRepository.getAllFriendsById(id);
        List<User> mutualFriends;

        if (userFriends != null && otherUserFriends != null) {
            List<Integer> otherUserFriendsId = friendsRepository.getAllFriendsById(otherId)
                    .stream()
                    .map(User::getId)
                    .toList();
            mutualFriends = userFriends
                    .stream()
                    .filter(user -> otherUserFriendsId.contains(user.getId()))
                    .toList();
        } else {
            log.info("У пользователей нет общих друзей");
            throw new NotFoundException("У вас с данным пользователем нет общих друзей.");
        }

        log.info("Список общих друзей получен");
        return mutualFriends.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public List<UserDto> getAllFriendsById(int id) {
        log.info("Начало процесса получения списка всех друзей");
        log.debug("Значения переменной при получении списка всех друзей id: {}", id);
        List<User> friends = friendsRepository.getAllFriendsById(id);
        log.info("Список всех друзей получен");
        return friends.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public List<UserDto> findAll() {
        log.info("Начало процесса получения списка всех пользователей");
        List<User> users = userRepository.findAll();
        log.info("Список всех пользователей получен");
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto create(UserDto userDto) {
        log.info("Начало процесса создания пользователя");

        if ((userDto.getName() == null) || (userDto.getName().isBlank())) {
            userDto.setName(userDto.getLogin());
        }

        User user = userRepository.create(UserMapper.mapToUser(userDto));
        log.info("Пользователь создан");
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(UserDto newUserDto) {
        log.info("Начало процесса обновления пользователя");

        if (newUserDto.getName() == null || newUserDto.getName().isBlank()) {
            newUserDto.setName(newUserDto.getLogin());
        }

        User user = userRepository.update(UserMapper.mapToUser(newUserDto));
        log.info("Пользователь обновлен");
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUser(int id) {
        log.info("Начало процесса получения пользователя с id = {}", id);
        User user = checkUser(id).orElseThrow(() -> {
            log.error("Пользователя с id {}, нет", id);
            return new NotFoundException("Пользователя с id " + id + " нет");
        });
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public void delete(int id) {
        log.info("Начало процесса удаления пользователя с id = {}", id);
        checkUser(id).orElseThrow(() -> {
            log.error("Пользователя с id {}, нет. Удаление не произошло", id);
            return new NotFoundException("Пользователя с id " + id + " нет");
        });
        userRepository.delete(id);
        log.info("User c id {}, успешно удален", id);
    }

    @Override
    public List<FilmDto> getRecommendationsFilms(int userId) {
        log.info("Начало процесса получения рекомендаций для пользователя с id = {}", userId);
        List<Integer> userLikedFilms = likesRepository.getIdFilmsLikedByUser(userId);
        List<Integer> userIds = userRepository.getAllUserWhoLikedFilms();

        if (userIds.isEmpty() || userLikedFilms.isEmpty()) {
            return new ArrayList<>();
        } else if (userIds.size() == 1) {
            return new ArrayList<>();
        }

        int countSameLikes = -1;
        int anotherUserId = userId;

        for (Integer id : userIds) {
            if (countSameLikes < getSizeCommonFilmsList(userId, id)) {
                countSameLikes = getSizeCommonFilmsList(userId, id);
                anotherUserId = id;
            }
        }

        if (countSameLikes < 0) {
            List<Integer> filmsExceptUserLiked = new ArrayList<>(filmRepository.findAll().stream()
                    .map(Film::getId)
                    .toList());
            filmsExceptUserLiked.retainAll(userLikedFilms);
            log.info("Рекомендации успешно получены");
            return filmsExceptUserLiked.stream()
                    .map(filmRepository::getFilmById)
                    .map(FilmMapper::mapToFilmDto)
                    .toList();
        }

        List<Integer> recommendationsId = likesRepository.getIdFilmsLikedByUser(anotherUserId);
        recommendationsId.removeAll(userLikedFilms);
        log.info("Рекомендации успешно получены");

        return recommendationsId
                .stream()
                .map(filmRepository::getFilmById)
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    private Optional<User> checkUser(int id) {
        try {
            User user = userRepository.getUser(id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    private int getSizeCommonFilmsList(int userId, int anotherId) {
        log.debug("Запущен вспомогателный метод getSizeCommonFilmsList, " +
                "для получения размера списка общих фильмов для пользователей " +
                "userId = {} и anotherId = {}", userId, anotherId);

        List<Integer> userFilm = likesRepository.getIdFilmsLikedByUser(userId);
        List<Integer> friendFilm = likesRepository.getIdFilmsLikedByUser(anotherId);

        if (friendFilm.size() > userFilm.size() && userId != anotherId) {
            friendFilm.retainAll(userFilm);
            return friendFilm.size();
        } else if (userFilm.size() > friendFilm.size()) {
            userFilm.retainAll(friendFilm);
            return -1 * userFilm.size();
        } else {
            return 0;
        }
    }
}