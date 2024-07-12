package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesRepository {

    List<Film> getPopularFilms(int count);

    List<Film> getPopularFilmsSortByGenreAndYear(int count, int genreId, int year);

    List<Film> getPopularFilmsSortByGenre(int count, int genreId);

    List<Film> getPopularFilmsSortByYear(int count, int year);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Integer> getListLikes(Film film);

    List<Integer> getIdFilmsLikedByUser(int userId);
}