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
@RequestMapping("films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<FilmDto> findAll() {
        log.info("Получен HTTP-запрос по адресу /films (метод GET). Вызван метод findAll()");
        return filmService.findAll();
    }

    @GetMapping("{id}")
    public FilmDto getFilmById(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /films/{id} (метод GET). "
                + "Вызван метод getFilmById(@PathVariable int id)");
        return filmService.getFilmById(id);
    }

    @GetMapping("director/{directorId}")
    public List<FilmDto> getFilmsByDirectorId(@PathVariable int directorId,
                                              @RequestParam(defaultValue = "year") String sortBy) {
        log.info("Получен HTTP-запрос по адресу /films (метод GET). "
                + "Вызван метод getFilmsByDirectorId(@PathVariable int directorId, " +
                "@RequestParam(defaultValue = \"year\") String sortBy)");
        return filmService.getFilmsByDirectorId(directorId, sortBy);
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

    @DeleteMapping("{filmId}")
    public void delete(@PathVariable int filmId) {
        log.info("Получен HTTP-запрос по адресу /films/{filmId} (метод DELETE). "
                + "Вызван метод delete(@PathVariable int filmId)");
        filmService.delete(filmId);
    }

    @GetMapping("search")
    public List<FilmDto> getPopularFilmsBySearchParam(@RequestParam String query,
                                                      @RequestParam("by") List<String> searchParams) {
        log.info("Получен HTTP-запрос по адресу films/search (метод GET)." +
                " Вызван метод getPopularFilmsBySearchParam(@RequestParam String query, " +
                "@RequestParam List<String> searchParams)");
        return filmService.getPopularFilmsBySearchParam(query, searchParams);
    }
}