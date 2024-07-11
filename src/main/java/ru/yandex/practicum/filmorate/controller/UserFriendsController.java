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
    public List<UserDto> getAllFriendsById(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends (метод GET). "
                + "Вызван метод getAllFriendsById(@PathVariable int id)");
        return userService.getAllFriendsById(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<UserDto> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends/common/{otherId} (метод GET). "
                + "Вызван метод getMutualFriends(@PathVariable int id, @PathVariable int otherId)");
        return userService.getMutualFriends(id, otherId);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends/{friendId} (метод PUT). "
                + "Вызван метод addFriend(@PathVariable int id, @PathVariable int friendId)");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/friends/{friendId} (метод DELETE). "
                + "Вызван метод deleteFriend(@PathVariable int id, @PathVariable int friendId)");
        userService.deleteFriend(id, friendId);
    }
}