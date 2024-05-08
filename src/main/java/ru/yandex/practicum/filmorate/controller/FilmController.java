package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmManager;

import java.util.Collection;


@RestController
@RequestMapping("/films")
public final class FilmController {
    private final FilmManager filmManager = new FilmManager();

    @GetMapping
    public Collection<Film> findAll() {
        return filmManager.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        return filmManager.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film newFilm) {
        return filmManager.update(newFilm);
    }
}