package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.indb.FilmRepository;
import ru.yandex.practicum.filmorate.storage.indb.GenreRepository;
import ru.yandex.practicum.filmorate.storage.indb.MpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@Primary
public class FilmServiceImpForDb implements FilmService {
    private static final LocalDate MINIMUM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final FilmRepository filmRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public FilmServiceImpForDb(FilmRepository filmRepository,
                               MpaRepository mpaRepository,
                               GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.mpaRepository = mpaRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public FilmDto addLike(int filmId, int userId) {
        log.info("Начало процесса добавление лайка");
        log.debug("Значение переменных при добавлении лайка filmId и userId: " + filmId + ", " + userId);
        Film film = filmRepository.addLike(filmId, userId);
        return FilmMapper.mapToFilmDto(film,
                genreRepository.getListGenre(film.getGenre()),
                mpaRepository.getMpa(film.getMpa()));
    }

    @Override
    public FilmDto deleteLike(int filmId, int userId) {
        log.info("Начало процесса удаления лайка");
        log.debug("Значение переменных при удалении лайка filmId и userId: " + filmId + ", " + userId);
        Film film = filmRepository.deleteLike(filmId, userId);
        return FilmMapper.mapToFilmDto(film,
                genreRepository.getListGenre(film.getGenre()),
                mpaRepository.getMpa(film.getMpa()));
    }

    @Override
    public List<FilmDto> getFilms(int count) {
        log.info("Начало процесса получения списка фильмов");
        log.debug("Значение переменной count: " + count);
        List<Film> popularFilms = filmRepository.findPopularFilms(count);
        log.info("Список сформирован");
        return popularFilms
                .stream()
                .map(film -> FilmMapper.mapToFilmDto(film,
                        genreRepository.getListGenre(film.getGenre()),
                        mpaRepository.getMpa(film.getMpa())))
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

        if (film.getReleaseDate().isBefore(MINIMUM_RELEASE_DATE)) {
            log.error("Дата релиза фильма при создании до 28 декабря 1895 г.");
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 г.");
        }

        Film cratedFilm = filmRepository.create(FilmMapper.mapToFilm(film));
        return FilmMapper.mapToFilmDto(cratedFilm,
                genreRepository.getListGenre(cratedFilm.getGenre()),
                mpaRepository.getMpa(cratedFilm.getMpa()));
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

        if (newFilm.getReleaseDate().isBefore(MINIMUM_RELEASE_DATE)) {
            log.error("Дата релиза фильма при создании до 28 декабря 1895 г.");
            throw new ValidationException("Дата релиза фильма должна быть не раньше 28 декабря 1895 г.");
        }

        Film film = filmRepository.update(FilmMapper.mapToFilm(newFilm));
        return FilmMapper.mapToFilmDto(film,
                genreRepository.getListGenre(film.getGenre()),
                mpaRepository.getMpa(film.getMpa()));
    }

    @Override
    public FilmDto getFilm(int filmId) {
        log.info("Начало процесса получения фильма по filmId = " + filmId);
        Film film = filmRepository.getFilm(filmId);
        return FilmMapper.mapToFilmDto(film,
                genreRepository.getListGenre(film.getGenre()),
                mpaRepository.getMpa(film.getMpa()));
    }

    @Override
    public Collection<FilmDto> findAll() {
        log.info("Начало процесса получения всех фильмов");
        return filmRepository.findAll()
                .stream()
                .map(film -> FilmMapper.mapToFilmDto(film,
                        genreRepository.getListGenre(film.getGenre()),
                        mpaRepository.getMpa(film.getMpa())))
                .toList();
    }
}