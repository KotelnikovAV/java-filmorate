package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorRepository {

    List<Director> getAllDirectors();

    Director getDirector(int directorId);

    Director create(Director director);

    Director update(Director newDirector);

    void delete(int id);

    void checkDirector(int directorId);

    List<Director> getListDirector(String listId);
}
