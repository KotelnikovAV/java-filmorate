package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;


@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmServiceImp;

    public FilmController(FilmStorage filmStorage, FilmService filmServiceImp) {
        this.filmStorage = filmStorage;
        this.filmServiceImp = filmServiceImp;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/popular")
    public Collection<Film> getFilms(@RequestParam (defaultValue = "10") int count) {
        return filmServiceImp.getFilms(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        return filmStorage.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id,
                        @PathVariable long userId) {
        return filmServiceImp.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id,
                           @PathVariable long userId) {
        return filmServiceImp.deleteLike(id, userId);
    }
}