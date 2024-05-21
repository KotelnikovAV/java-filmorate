package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен HTTP-запрос по адресу /users (метод GET). Вызван метод findAll()");
        return userService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        log.info("Получен HTTP-запрос по адресу /users (метод Post). "
                + "Вызван метод create(@Valid @RequestBody User user)");
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Получен HTTP-запрос по адресу /users (метод Put). "
                + "Вызван метод update(@Valid @RequestBody User newUser)");
        return userService.update(newUser);
    }
}