package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    List<Film> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    Optional<Film> getFilmById(int filmId);

    void delete(int filmId);
}
