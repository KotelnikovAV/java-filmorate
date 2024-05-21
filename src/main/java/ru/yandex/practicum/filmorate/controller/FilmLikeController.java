package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmLikeController {
    private final FilmService filmService;

    public FilmLikeController(FilmService filmServiceImp) {
        this.filmService = filmServiceImp;
    }

    @GetMapping("/popular")
    public Collection<Film> getFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен HTTP-запрос по адресу /films/popular (метод GET). "
                + "Вызван метод getFilms(@RequestParam(defaultValue = \"10\") int count)");
        log.debug("Полученный параметр запроса count = " + count);
        return filmService.getFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id,
                        @PathVariable long userId) {
        log.info("Получен HTTP-запрос по адресу /films/{id}/like/{userId} (метод Put). Вызван метод "
                + "addLike(@PathVariable long id, @PathVariable long userId)");
        log.debug("Полученные переменные пути при Put запросе id = " + id + ", userId = " + userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id,
                           @PathVariable long userId) {
        log.info("Получен HTTP-запрос по адресу /films/{id}/like/{userId} (метод Delete). Вызван метод "
                + "deleteLike(@PathVariable long id, @PathVariable long userId)");
        log.debug("Полученные переменные пути при Delete запросе id = " + id + ", userId = " + userId);
        return filmService.deleteLike(id, userId);
    }
}