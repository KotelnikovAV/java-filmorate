package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("films")
public class FilmLikeController {
    private final FilmService filmService;

    @GetMapping("popular")
    public List<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") int count,
                                         @RequestParam(defaultValue = "0") int genreId,
                                         @RequestParam(defaultValue = "0") int year) {
        log.info("Получен HTTP-запрос по адресу /films/popular?count={limit}&genreId={genreId}&year={year} (метод GET). "
                + "Вызван метод getPopularFilmsSortByGenreAndYear(@RequestParam(defaultValue = \"10\") int count, " +
                "@RequestParam Optional<Integer> genreId, @RequestParam Optional<Integer> year)");
        return filmService.getPopularFilms(count, genreId, year);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен HTTP-запрос по адресу /films/{id}/like/{userId} (метод PUT). Вызван метод "
                + "addLike(@PathVariable int id, @PathVariable int userId)");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен HTTP-запрос по адресу /films/{id}/like/{userId} (метод DELETE). Вызван метод "
                + "deleteLike(@PathVariable int id, @PathVariable int userId)");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/common")
    public List<FilmDto> getCommonFilms(@RequestParam("userId") int userId, @RequestParam("friendId") int friendId) {
        log.info("Получен HTTP-запрос по адресу /films/common?userId={userId}&friendId={friendId}. " +
                "Вызван метод getCommonFilms(@PathVariable int userId, @PathVariable int friendId");
        return filmService.getCommonFilms(userId, friendId);
    }
}