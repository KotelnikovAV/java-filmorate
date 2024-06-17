package ru.yandex.practicum.filmorate.storage.indb;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaRepository {

    List<Mpa> getAllMpa();

    Mpa getMpa(int id);

    void checkMpa(int id);
}
