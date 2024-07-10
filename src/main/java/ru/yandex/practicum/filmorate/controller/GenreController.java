package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<GenreDto> getAllGenres() {
        log.info("Получен HTTP-запрос по адресу /genres (метод GET). Вызван метод getAllGenres()");
        return genreService.getAllGenres();
    }

    @GetMapping("{id}")
    public GenreDto getGenre(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /genres/{id} (метод GET). Вызван метод getGenre(id)");
        return genreService.getGenre(id);
    }
}
