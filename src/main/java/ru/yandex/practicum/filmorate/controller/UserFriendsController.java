package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserFriendsController {
    private final UserService userService;

    @GetMapping("{id}/friends")
    public List<UserDto> getAllFriends(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends (метод GET). "
                + "Вызван метод getAllFriendsById(@PathVariable int id)");
        log.debug("Полученная переменная пути id = " + id);
        return userService.getAllFriendsById(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<UserDto> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends/common/{otherId} (метод GET). "
                + "Вызван метод getMutualFriends(@PathVariable int id, @PathVariable int otherId)");
        log.debug("Полученные переменные пути при Put запросе id = " + id + ", otherId = " + otherId);
        return userService.getMutualFriends(id, otherId);
    }

    @PutMapping("{id}/friends/{friendId}")
    public UserDto addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends/{friendId} (метод PUT). "
                + "Вызван метод addFriend(@PathVariable int id, @PathVariable int friendId)");
        log.debug("Полученные переменные пути при Put запросе id = " + id + ", friendId = " + friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public UserDto deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends/{friendId} (метод DELETE). "
                + "Вызван метод deleteFriend(@PathVariable int id, @PathVariable int friendId)");
        log.debug("Полученные переменные пути при Delete запросе id = " + id + ", friendId = " + friendId);
        return userService.deleteFriend(id, friendId);
    }
}