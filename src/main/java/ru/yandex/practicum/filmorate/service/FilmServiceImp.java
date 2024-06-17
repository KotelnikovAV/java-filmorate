package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inmemory.FilmStorage;
import ru.yandex.practicum.filmorate.storage.inmemory.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmServiceImp implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImp(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public FilmDto addLike(int filmId, int userId) {
        log.info("Начало процесса добавление лайка");
        log.debug("Значение переменных при добавлении лайка filmId и userId: " + filmId + ", " + userId);
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        List<Integer> likes = film.getLikes();

        if (likes.contains(userId)) {
            log.info("Пользователь уже поставил лайк");
            throw new DuplicatedDataException("Пользователь "
                    + user.getName() + "уже поставил лайк фильму " + film.getName() + ".");
        }

        likes.add(userId);
        log.info("Лайк добавлен");
        return FilmMapper.mapToFilmDto(film, null, null);
    }

    @Override
    public FilmDto deleteLike(int filmId, int userId) {
        log.info("Начало процесса удаления лайка");
        log.debug("Значение переменных при удалении лайка filmId и userId: " + filmId + ", " + userId);
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        List<Integer> likes = film.getLikes();

        if (!likes.contains(userId)) {
            log.info("Пользователь не ставил лайк этому фильму");
            throw new NotFoundException("Пользователь "
                    + user.getName() + "не ставил лайк фильму " + film.getName() + ".");
        }

        likes.remove(userId);
        log.info("Лайк удален");
        return FilmMapper.mapToFilmDto(film, null, null);
    }

    @Override
    public List<FilmDto> getFilms(int count) {
        log.info("Начало процесса получения списка фильмов");
        log.debug("Значение переменной count: " + count);
        List<Film> films = filmStorage.findAll()
                .stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size() * -1))
                .limit(count)
                .toList();
        log.info("Список сформирован");
        return films
                .stream()
                .map(film -> FilmMapper.mapToFilmDto(film,null,null))
                .toList();
    }

    @Override
    public FilmDto create(FilmDto film) {
        return FilmMapper.mapToFilmDto(filmStorage.create(FilmMapper.mapToFilm(film)), null, null);
    }

    @Override
    public FilmDto update(FilmDto newFilm) {
        return FilmMapper.mapToFilmDto(filmStorage.create(FilmMapper.mapToFilm(newFilm)), null, null);
    }

    @Override
    public FilmDto getFilm(int filmId) {
        return FilmMapper.mapToFilmDto(filmStorage.getFilm(filmId), null, null);
    }

    @Override
    public Collection<FilmDto> findAll() {
        return filmStorage.findAll()
                .stream()
                .map(film -> FilmMapper.mapToFilmDto(film,null,null))
                .toList();
    }
}