package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsRepository;
import ru.yandex.practicum.filmorate.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendsRepository friendsRepository;

    @Override
    public UserDto addFriend(int id, int friendId) {
        log.info("Начало процесса добавления друга");
        log.debug("Значения переменных при добавлении друга id и friendId: " + id + ", " + friendId);

        if (id == friendId) {
            log.info("Ошибка: значения id и friendId при добавлении в друзья совпадают");
            throw new ValidationException("Вы не можете добавить самого себя в друзья.");
        }

        return UserMapper.mapToUserDto(friendsRepository.addFriend(id, friendId));
    }

    @Override
    public UserDto deleteFriend(int id, int friendId) {
        log.info("Начало процесса удаления друга");
        log.debug("Значения переменных при удалении друга id и friendId: " + id + ", " + friendId);

        if (id == friendId) {
            log.info("Ошибка: значения id и friendId при удалении совпадают");
            throw new ValidationException("Вы не можете удалить самого себя из друзей.");
        }

        return UserMapper.mapToUserDto(friendsRepository.deleteFriend(id, friendId));
    }

    @Override
    public List<UserDto> getMutualFriends(int id, int otherId) {
        log.info("Начало процесса получения списка общих друзей");
        log.debug("Значения переменных при получении списка общих друзей id и otherId: " + id + ", " + otherId);

        if (id == otherId) {
            log.info("Ошибка: значения id и otherId при поиске общих друзей совпадают");
            throw new ValidationException("Вы не можете искать общих друзей с самим собой.");
        }

        List<User> userFriends = friendsRepository.getAllFriendsById(id);
        List<User> otherUserFriends = friendsRepository.getAllFriendsById(id);
        List<User> mutualFriends = new ArrayList<>();
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

        return mutualFriends
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public List<UserDto> getAllFriendsById(int id) {
        log.info("Начало процесса получения списка всех друзей");
        log.debug("Значения переменной при получении списка всех друзей id: " + id);
        return friendsRepository.getAllFriendsById(id)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public List<UserDto> findAll() {
        log.info("Начало процесса получения списка всех пользователей");
        return userRepository.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto create(UserDto userDto) {
        log.info("Начало процесса создания пользователя");

        if ((userDto.getName() == null) || (userDto.getName().isBlank())) {
            userDto.setName(userDto.getLogin());
        }

        return UserMapper.mapToUserDto(userRepository.create(UserMapper.mapToUser(userDto)));
    }

    @Override
    public UserDto update(UserDto newUserDto) {
        log.info("Начало процесса обновления пользователя");

        if (newUserDto.getName() == null || newUserDto.getName().isBlank()) {
            newUserDto.setName(newUserDto.getLogin());
        }
        return UserMapper.mapToUserDto(userRepository.update(UserMapper.mapToUser(newUserDto)));
    }

    @Override
    public UserDto getUser(int id) {
        log.info("Начало процесса получения пользователя с id = " + id);
        return UserMapper.mapToUserDto(userRepository.getUser(id));
    }
}