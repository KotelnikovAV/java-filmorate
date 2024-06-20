package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {

    List<Film> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    Film getFilmById(int filmId);

    List<Film> getPopularFilms(int count);

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    List<Integer> getListLikes(Film film);
}
