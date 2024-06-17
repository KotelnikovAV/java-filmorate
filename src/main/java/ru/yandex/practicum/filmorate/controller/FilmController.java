package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<FilmDto> findAll() {
        log.info("Получен HTTP-запрос по адресу /films (метод GET). Вызван метод findAll()");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /films/{id} (метод GET). "
                + "Вызван метод getFilmById(@PathVariable int id)");
        return filmService.getFilm(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@Valid @RequestBody FilmDto filmDto) {
        log.info("Получен HTTP-запрос по адресу /films (метод POST). "
                + "Вызван метод create(@Valid @RequestBody FilmDto filmDto)");
        return filmService.create(filmDto);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmDto filmDto) {
        log.info("Получен HTTP-запрос по адресу /films (метод PUT). "
                + "Вызван метод update(@Valid @RequestBody FilmDto filmDto)");
        return filmService.update(filmDto);
    }
}