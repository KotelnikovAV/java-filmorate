package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {

    List<Film> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    Film getFilmById(int filmId);
}
