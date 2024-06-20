package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaRepository mpaRepository;

    @Override
    public List<Mpa> getAllMpa() {
        log.info("Начало процесса получения всех рейтингов");
        return mpaRepository.getAllMpa();
    }

    @Override
    public Mpa getMpa(int id) {
        log.info("Начало процесса получения рейтинга по id = " + id);
        return mpaRepository.getMpa(id);
    }
}
