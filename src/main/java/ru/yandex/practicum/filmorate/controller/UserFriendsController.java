package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserFriendsController {
    private final UserService userService;

    public UserFriendsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable long id) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends (метод GET). "
                + "Вызван метод getAllFriends(@PathVariable long id)");
        log.debug("Полученная переменная пути id = " + id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable long id,
                                             @PathVariable long otherId) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends/common/{otherId} (метод GET). "
                + "Вызван метод getMutualFriends(@PathVariable long id, @PathVariable long otherId)");
        log.debug("Полученные переменные пути при Put запросе id = " + id + ", otherId = " + otherId);
        return userService.getMutualFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id,
                          @PathVariable long friendId) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends/{friendId} (метод Put). "
                + "Вызван метод addFriend(@PathVariable long id, @PathVariable long friendId)");
        log.debug("Полученные переменные пути при Put запросе id = " + id + ", friendId = " + friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id,
                             @PathVariable long friendId) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends/{friendId} (метод Delete). "
                + "Вызван метод deleteFriend(@PathVariable long id, @PathVariable long friendId)");
        log.debug("Полученные переменные пути при Delete запросе id = " + id + ", friendId = " + friendId);
        return userService.deleteFriend(id, friendId);
    }
}