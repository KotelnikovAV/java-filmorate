package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private static final LocalDate MINIMUM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final FilmRepository filmRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final LikesRepository likesRepository;
    private final DirectorRepository directorRepository;

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

    //add-common-films
    public List<FilmDto> getCommonFilms(int userId, int friendId) {
        log.info("Начало процесса получения списка общих фильмов");
        log.debug("Значение переменной userID: {}, значение переменной friendId: {}", userId, friendId);
        List<Film> userFilm = likesRepository.getCommonFilms(userId, friendId);
        log.info("Список общих фильмов получен");
        return userFilm.stream().map(FilmMapper::mapToFilmDto).toList();

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
        Film film = checkFilm(filmId).orElseThrow(() -> {
            log.error("Фильма с id {}, нет", filmId);
            return new NotFoundException("Фильма с id " + filmId + " нет");
        });
        log.info("Фильм получен");
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public void delete(int filmId) {
        log.info("Начало процесса удаления фильма по filmId = " + filmId);
        checkFilm(filmId).orElseThrow(() -> {
            log.error("Фильма с id {}, нет", filmId);
            return new NotFoundException("Фильма с id " + filmId + " нет");
        });
        filmRepository.delete(filmId);
        log.info("Фильм успешно удален.");
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


    private Optional<Film> checkFilm(int id) {
        try {
            Film film = filmRepository.getFilmById(id);
            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<FilmDto> getFilmsByDirectorId(int directorId, String sortBy) {
        directorRepository.checkDirector(directorId);

        if (sortBy.equals("year")) {
            log.info("Начало процесса получения фильмов, отсортированных по дате релиза, режиссер которых является " +
                    "directorId = " + directorId);
            List<FilmDto> films = filmRepository.getFilmsByDirectorIdSortByYear(directorId).stream()
                    .map(FilmMapper::mapToFilmDto)
                    .toList();
            log.info("Получен список фильмов отсортированных по дате релиза");
            return films;
        } else if (sortBy.equals("likes")) {
            log.info("Начало процесса получения фильмов, отсортированных по лайкам, режиссер которых является " +
                    "directorId = " + directorId);
            List<FilmDto> films = filmRepository.getFilmsByDirectorIdSortByLikes(directorId).stream()
                    .map(FilmMapper::mapToFilmDto)
                    .toList();
            log.info("Получен список фильмов отсортированных по лайкам");
            return films;
        } else {
            throw new NotFoundException("Выбран неверный метод сортировки");

        }
    }
}