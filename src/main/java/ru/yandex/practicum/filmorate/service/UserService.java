package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UserDto;

import java.util.List;

public interface UserService {

    void addFriend(int id, int friendId);

    void deleteFriend(int id, int friendId);

    List<UserDto> getMutualFriends(int id, int otherId);

    List<UserDto> getAllFriendsById(int id);

    List<UserDto> findAll();

    UserDto create(UserDto userDto);

    UserDto update(UserDto newUserDto);

    UserDto getUser(int id);

    void delete(int id);

    List<FilmDto> getRecommendationsFilms(int userId);
}