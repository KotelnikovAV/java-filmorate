package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmRepository;
import ru.yandex.practicum.filmorate.storage.GenreRepository;
import ru.yandex.practicum.filmorate.storage.LikesRepository;
import ru.yandex.practicum.filmorate.storage.MpaRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private static final LocalDate MINIMUM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final FilmRepository filmRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final LikesRepository likesRepository;

    @Override
    public FilmDto addLike(int filmId, int userId) {
        log.info("Начало процесса добавление лайка");
        log.debug("Значение переменных при добавлении лайка filmId и userId: " + filmId + ", " + userId);
        Film film = likesRepository.addLike(filmId, userId);
        log.info("Лайк поставлен");
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto deleteLike(int filmId, int userId) {
        log.info("Начало процесса удаления лайка");
        log.debug("Значение переменных при удалении лайка filmId и userId: " + filmId + ", " + userId);
        Film film = likesRepository.deleteLike(filmId, userId);
        log.info("Лайк удален");
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public List<FilmDto> getPopularFilms(int count) {
        log.info("Начало процесса получения списка популярных фильмов");
        log.debug("Значение переменной count: " + count);
        List<Film> popularFilms = likesRepository.getPopularFilms(count);
        log.info("Список популярных фильмов получен");
        return popularFilms.stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
    }

    @Override
    public FilmDto create(FilmDto film) {
        log.info("Начало процесса создания фильма");

        try {
            if (film.getMpa() != null) {
                mpaRepository.checkMpa(film.getMpa().getId());
            }

            if (film.getGenres() != null) {
                film.getGenres().forEach(genre -> genreRepository.checkGenre(genre.getId()));
            }
        } catch (NotFoundException e) {
            throw new ValidationException(e.getMessage());
        }

        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(MINIMUM_RELEASE_DATE)) {
                log.error("Дата релиза фильма при создании до 28 декабря 1895 г.");
                throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 г.");
            }
        }

        Film cratedFilm = filmRepository.create(FilmMapper.mapToFilm(film));
        log.info("Фильм добавлен");
        return FilmMapper.mapToFilmDto(cratedFilm);
    }

    @Override
    public FilmDto update(FilmDto newFilm) {
        log.info("Начало процесса обновления фильма");
        try {
            if (newFilm.getMpa() != null) {
                mpaRepository.checkMpa(newFilm.getMpa().getId());
            }

            if (newFilm.getGenres() != null) {
                newFilm.getGenres().forEach(genre -> genreRepository.checkGenre(genre.getId()));
            }
        } catch (NotFoundException e) {
            throw new ValidationException(e.getMessage());
        }

        if (newFilm.getReleaseDate() != null) {
            if (newFilm.getReleaseDate().isBefore(MINIMUM_RELEASE_DATE)) {
                log.error("Дата релиза фильма при обновлении до 28 декабря 1895 г.");
                throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 г.");
            }
        }

        Film film = filmRepository.update(FilmMapper.mapToFilm(newFilm));
        log.info("Фильм обновлен");
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto getFilmById(int filmId) {
        log.info("Начало процесса получения фильма по filmId = " + filmId);
        Film film = filmRepository.getFilmById(filmId);
        log.info("Фильм получен");
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public List<FilmDto> findAll() {
        log.info("Начало процесса получения всех фильмов");
        List<FilmDto> films = filmRepository.findAll()
                .stream()
                .map(FilmMapper::mapToFilmDto)
                .toList();
        log.info("Список всех фильмов получен");
        return films;
    }
}