package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("directors")
public class DirectorController {
    private final DirectorService directorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DirectorDto create(@Valid @RequestBody DirectorDto director) {
        log.info("Получен HTTP-запрос по адресу /directors (метод POST). Вызван метод create(director)");
        return directorService.create(director);
    }

    @PutMapping
    public DirectorDto update(@Valid @RequestBody DirectorDto newDirector) {
        log.info("Получен HTTP-запрос по адресу /directors (метод PUT). Вызван метод update(newDirector)");
        return directorService.update(newDirector);
    }

    @GetMapping
    public List<DirectorDto> getAllDirectors() {
        log.info("Получен HTTP-запрос по адресу /directors (метод GET). Вызван метод getAllDirectors()");
        return directorService.getAllDirectors();
    }

    @GetMapping("{id}")
    public DirectorDto getDirector(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /directors/{id} (метод GET). Вызван метод getDirector(id)");
        return directorService.getDirector(id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /directors/{id} (метод DELETE). Вызван метод delete(id)");
        directorService.delete(id);
    }
}
