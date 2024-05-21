package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmServiceImp implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmServiceImp(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film addLike(long filmId, long userId) {
        log.info("Начало процесса добавление лайка");
        log.debug("Значение переменных при добавлении лайка filmId и userId: " + filmId + ", " + userId);
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        List<Long> likes = film.getLikes();

        if (likes.contains(userId)) {
            log.info("Пользователь уже поставил лайк");
            throw new DuplicatedDataException("Пользователь "
                    + user.getName() + "уже поставил лайк фильму " + film.getName() + ".");
        }

        likes.add(userId);
        log.info("Лайк добавлен");
        return film;
    }

    @Override
    public Film deleteLike(long filmId, long userId) {
        log.info("Начало процесса удаления лайка");
        log.debug("Значение переменных при удалении лайка filmId и userId: " + filmId + ", " + userId);
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        List<Long> likes = film.getLikes();

        if (!likes.contains(userId)) {
            log.info("Пользователь не ставил лайк этому фильму");
            throw new NotFoundException("Пользователь "
                    + user.getName() + "не ставил лайк фильму " + film.getName() + ".");
        }

        likes.remove(userId);
        log.info("Лайк удален");
        return film;
    }

    @Override
    public List<Film> getFilms(int count) {
        log.info("Начало процесса получения списка фильмов");
        log.debug("Значение переменной count: " + count);
        List<Film> films = filmStorage.findAll()
                .stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size() * -1))
                .limit(count)
                .toList();
        log.info("Список сформирован");
        return films;
    }

    @Override
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    @Override
    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    @Override
    public Film getFilm(long filmId) {
        return filmStorage.getFilm(filmId);
    }

    @Override
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }
}