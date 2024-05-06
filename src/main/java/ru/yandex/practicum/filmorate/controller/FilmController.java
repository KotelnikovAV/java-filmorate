package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public final class FilmController {
    private static final int MAXIMUM_DESCRIPTION_LENGTH = 200;
    private static final LocalDate MINIMUM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Начало процесса создания нового фильма");

        if (film.getDescription().length() > MAXIMUM_DESCRIPTION_LENGTH) {
            log.error("Описание фильма при создании содержит более 200 символов");
            throw new ValidationException("Описание должно содержать меньше 200 символов");
        } else if (film.getReleaseDate().isBefore(MINIMUM_RELEASE_DATE)) {
            log.error("Дата релиза фильма при создании до 28 декабря 1895 г.");
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 г.");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен");
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film newFilm) {
        log.info("Начало процесса обновления фильма");

        if (newFilm.getDescription().length() > MAXIMUM_DESCRIPTION_LENGTH) {
            log.error("Описание фильма при обновлении содержит более 200 символов");
            throw new ValidationException("Описание должно содержать меньше 200 символов");
        } else if (newFilm.getReleaseDate().isBefore(MINIMUM_RELEASE_DATE)) {
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