package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmServiceImp) {
        this.filmService = filmServiceImp;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен HTTP-запрос по адресу /films (метод GET). Вызван метод findAll()");
        return filmService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен HTTP-запрос по адресу /films (метод Post). "
                + "Вызван метод create(@Valid @RequestBody Film film)");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Получен HTTP-запрос по адресу /films (метод Put). "
                + "Вызван метод update(@Valid @RequestBody Film newFilm)");
        return filmService.update(newFilm);
    }
}