package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserManager;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public final class UserController {
    private final UserManager userManager = new UserManager();

    @GetMapping
    public Collection<User> findAll() {
        return userManager.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        return userManager.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody final User newUser) {
        return userManager.update(newUser);
    }
}