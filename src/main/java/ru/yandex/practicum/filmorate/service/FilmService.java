package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    Film addLike(long filmId, long userId);

    Film deleteLike(long filmId, long userId);

    List<Film> getFilms(int count);

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    Film getFilm(long filmId);
}