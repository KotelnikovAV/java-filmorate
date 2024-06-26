package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Получен HTTP-запрос по адресу /users (метод GET). Вызван метод findAll()");
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info("Получен HTTP-запрос по адресу /users (метод POST). "
                + "Вызван метод create(@Valid @RequestBody UserDto userDto)");
        return userService.create(userDto);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserDto newUserDto) {
        log.info("Получен HTTP-запрос по адресу /users (метод PUT). "
                + "Вызван метод update(@Valid @RequestBody UserDto newUserDto)");
        return userService.update(newUserDto);
    }
}