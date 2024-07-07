package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesRepository {

    List<Film> getPopularFilms(int count);

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    List<Integer> getListLikes(Film film);

    List<Film> getCommonFilms(int userId, int friendId);

    List<Integer> getIdFilmsLikedByUser(int userId);
    //add-recommendations
    List<Integer> getAllUserWhoLikedFilms();
}
