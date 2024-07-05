package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Query;

import java.sql.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilmRepositoryImpl implements FilmRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapperFilm;

    @Override
    public List<Film> findAll() {
        log.info("Отправка запроса FIND_ALL_FILMS");
        return jdbc.query(Query.FIND_ALL_FILMS.getQuery(), mapperFilm);
    }

    @Override
    public Film create(Film film) {
        log.info("Отправка запроса INSERT_FILM");

        int id = BaseDbStorage.insert(
                jdbc,
                Query.INSERT_FILM.getQuery(),
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                convertGenresToString(film.getGenre()),
                film.getMpa().getId()
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        log.info("Отправка запроса UPDATE_FILM");
        int rowsUpdated = jdbc.update(
                Query.UPDATE_FILM.getQuery(),
                newFilm.getName(),
                newFilm.getDescription(),
                Date.valueOf(newFilm.getReleaseDate()),
                newFilm.getDuration(),
                convertGenresToString(newFilm.getGenre()),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );

        if (rowsUpdated == 0) {
            throw new NotFoundException("Такого фильма нет");
        }

        return newFilm;
    }

    @Override
    public void delete(int filmId) {
        log.info("Отправка запроса DELETE_FILMS_LIKE");
        jdbc.update(Query.DELETE_FILMS_LIKE.getQuery(), filmId);
        log.info("Отправка запроса DELETE_FILM");
        jdbc.update(Query.DELETE_FILM.getQuery(), filmId);
    }

    @Override
    public Film getFilmById(int filmId) {
        log.info("Отправка запроса FIND_FILM_BY_ID");
        return jdbc.queryForObject(Query.FIND_FILM_BY_ID.getQuery(), mapperFilm, filmId);
    }

    private String convertGenresToString(List<Genre> genres) {
        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";

        if (genres != null) {
            for (Genre genre : genres) {
                stringBuilder.append(prefix);
                prefix = ", ";
                stringBuilder.append(genre.getId());
            }
        }

        return stringBuilder.toString();
    }
}
