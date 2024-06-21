package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;


public interface GenreRepository {

    List<Genre> getAllGenres();

    Genre getGenre(int id);

    void checkGenre(int id);

    List<Genre> getListGenre(String listId);
}
