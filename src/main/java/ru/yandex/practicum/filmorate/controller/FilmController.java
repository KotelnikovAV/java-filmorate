package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<FilmDto> findAll() {
        log.info("Получен HTTP-запрос по адресу /films (метод GET). Вызван метод findAll()");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /films/{id} (метод GET). "
                + "Вызван метод getFilmById(@PathVariable int id)");
        return filmService.getFilmById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmDto create(@Valid @RequestBody FilmDto filmDto) {
        log.info("Получен HTTP-запрос по адресу /films (метод POST). "
                + "Вызван метод create(@Valid @RequestBody FilmDto requestFilmDto)");
        return filmService.create(filmDto);
    }

    @PutMapping
    public FilmDto update(@Valid @RequestBody FilmDto filmDto) {
        log.info("Получен HTTP-запрос по адресу /films (метод PUT). "
                + "Вызван метод update(@Valid @RequestBody FilmDto requestFilmDto)");
        return filmService.update(filmDto);
    }
}