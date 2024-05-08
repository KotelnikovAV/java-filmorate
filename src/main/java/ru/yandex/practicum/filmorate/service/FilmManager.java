package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class FilmManager {
    private static final LocalDate MINIMUM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films = new HashMap<>();

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film create(Film film) {
        log.info("Начало процесса создания нового фильма");

        if (film.getReleaseDate().isBefore(MINIMUM_RELEASE_DATE)) {
            log.error("Дата релиза фильма при создании до 28 декабря 1895 г.");
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 г.");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    public Film update(Film newFilm) {
        log.info("Начало процесса обновления фильма");

        if (newFilm.getReleaseDate().isBefore(MINIMUM_RELEASE_DATE)) {
            log.error("Дата релиза фильма при обновлении до 28 декабря 1895 г.");
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 г.");
        } else if (!films.containsKey(newFilm.getId())) {
            log.error("При обновлении не был найден фильм");
            throw new NotFoundException("Фильма с id = " + newFilm.getId() + " не существует");
        }

        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDuration(newFilm.getDuration());
        log.info("Фильм обновлен");
        return oldFilm;
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .max(Comparator.comparingInt(id -> id))
                .orElse(0);
        return ++currentMaxId;
    }
}