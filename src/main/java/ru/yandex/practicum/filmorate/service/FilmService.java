package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film addLike(long filmId, long userId);

    Film deleteLike(long filmId, long userId);

    List<Film> getFilms(int count);
}