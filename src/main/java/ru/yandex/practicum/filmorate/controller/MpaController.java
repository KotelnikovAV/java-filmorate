package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<Mpa> getListMpa() {
        log.info("Получен HTTP-запрос по адресу /mpa (метод GET). Вызван метод getAllMpa()");
        return mpaService.getAllMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /mpa/{id} (метод GET). Вызван метод getMpa(id)");
        return mpaService.getMpa(id);
    }
}
