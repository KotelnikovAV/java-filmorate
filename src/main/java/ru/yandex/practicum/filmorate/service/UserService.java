package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.dto.UserEventDto;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface UserService {

    UserDto addFriend(int id, int friendId);

    UserDto deleteFriend(int id, int friendId);

    List<UserDto> getMutualFriends(int id, int otherId);

    List<UserDto> getAllFriendsById(int id);

    List<UserDto> findAll();

    UserDto create(UserDto userDto);

    UserDto update(UserDto newUserDto);

    UserDto getUser(int id);

    void delete(int id);

    List<Film> getRecommendationsFilms(int userId);
  
    List<FilmDto> getRecommendationsFilms(int userId);
}