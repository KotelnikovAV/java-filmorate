package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmServiceImp implements FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public FilmServiceImp(FilmStorage inMemoryFilmStorage, UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = (InMemoryFilmStorage) inMemoryFilmStorage;
        this.inMemoryUserStorage = (InMemoryUserStorage) inMemoryUserStorage;
    }

    @Override
    public Film addLike(long filmId, long userId) {
        log.info("Начало процесса добавление лайка");
        log.debug("Значение переменных при добавлении лайка filmId и userId: " + filmId + ", " + userId);
        Film film = inMemoryFilmStorage.getFilm(filmId);
        User user = inMemoryUserStorage.getUser(userId);
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
        Film film = inMemoryFilmStorage.getFilm(filmId);
        User user = inMemoryUserStorage.getUser(userId);
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
        List<Film> films = inMemoryFilmStorage.findAll()
                .stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size() * -1))
                .limit(count)
                .toList();
        log.info("Список сформирован");
        return films;
    }
}