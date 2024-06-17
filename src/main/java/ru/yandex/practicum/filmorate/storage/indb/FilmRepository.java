package ru.yandex.practicum.filmorate.storage.indb;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmRepository {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    Film getFilm(int filmId);

    List<Film> findPopularFilms(int count);

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);
}
