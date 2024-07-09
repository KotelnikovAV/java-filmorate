package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User create(User user);

    User update(User newUser);

    User getUser(int id);

    void delete(int id);

    List<FilmDto> getRecommendationsFilms(int userId);

    List<Integer> findRecommendationsId(int userId);
}