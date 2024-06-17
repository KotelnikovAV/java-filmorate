package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmLikeController {
    private final FilmService filmService;

    @Autowired
    public FilmLikeController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/popular")
    public Collection<FilmDto> getFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен HTTP-запрос по адресу /films/popular (метод GET). "
                + "Вызван метод getFilms(@RequestParam(defaultValue = \"10\") int count)");
        log.debug("Полученный параметр запроса count = " + count);
        return filmService.getFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public FilmDto addLike(@PathVariable int id,
                        @PathVariable int userId) {
        log.info("Получен HTTP-запрос по адресу /films/{id}/like/{userId} (метод PUT). Вызван метод "
                + "addLike(@PathVariable int id, @PathVariable int userId)");
        log.debug("Полученные переменные пути при Put запросе id = " + id + ", userId = " + userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public FilmDto deleteLike(@PathVariable int id,
                           @PathVariable int userId) {
        log.info("Получен HTTP-запрос по адресу /films/{id}/like/{userId} (метод DELETE). Вызван метод "
                + "deleteLike(@PathVariable int id, @PathVariable int userId)");
        log.debug("Полученные переменные пути при Delete запросе id = " + id + ", userId = " + userId);
        return filmService.deleteLike(id, userId);
    }
}