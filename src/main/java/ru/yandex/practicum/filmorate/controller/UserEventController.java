package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.UserEventDto;
import ru.yandex.practicum.filmorate.service.UserEventService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserEventController {
    private final UserEventService userEventService;

    @GetMapping("{id}/feed")
    public List<UserEventDto> getAllUserEvents(@Valid @PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /users/{id}/feed (метод GET). "
                + "Вызван метод getAllUserEvents(@PathVariable int id)");
        return userEventService.getAllUserEvents(id);
    }
}