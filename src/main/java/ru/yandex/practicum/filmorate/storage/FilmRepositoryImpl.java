package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Query;

import java.sql.Date;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilmRepositoryImpl implements FilmRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapperFilm;
    private final RowMapper<Director> directorRowMapper;

    @Override
    public List<Film> findAll() {
        log.info("Отправка запроса FIND_ALL_FILMS");
        return jdbc.query(Query.FIND_ALL_FILMS.getQuery(), mapperFilm);
    }

    @Override
    public Film create(Film film) {
        int id;
        log.info("Отправка запроса INSERT_FILM");

        if (film.getDirectors() != null) {
            id = BaseDbStorage.insert(
                    jdbc,
                    Query.INSERT_FILM.getQuery(),
                    film.getName(),
                    film.getDescription(),
                    Date.valueOf(film.getReleaseDate()),
                    film.getDuration(),
                    convertGenresToString(film.getGenre()),
                    film.getMpa().getId(),
                    convertDirectorsToString(film.getDirectors())
            );
        } else {
            id = BaseDbStorage.insert(
                    jdbc,
                    Query.INSERT_FILM.getQuery(),
                    film.getName(),
                    film.getDescription(),
                    Date.valueOf(film.getReleaseDate()),
                    film.getDuration(),
                    convertGenresToString(film.getGenre()),
                    film.getMpa().getId(),
                    null
            );
        }

        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        int rowsUpdated;
        log.info("Отправка запроса UPDATE_FILM");

        if (newFilm.getDirectors() != null) {
            rowsUpdated = jdbc.update(
                    Query.UPDATE_FILM.getQuery(),
                    newFilm.getName(),
                    newFilm.getDescription(),
                    Date.valueOf(newFilm.getReleaseDate()),
                    newFilm.getDuration(),
                    convertGenresToString(newFilm.getGenre()),
                    newFilm.getMpa().getId(),
                    convertDirectorsToString(newFilm.getDirectors()),
                    newFilm.getId()
            );
        } else {
            rowsUpdated = jdbc.update(
                    Query.UPDATE_FILM.getQuery(),
                    newFilm.getName(),
                    newFilm.getDescription(),
                    Date.valueOf(newFilm.getReleaseDate()),
                    newFilm.getDuration(),
                    convertGenresToString(newFilm.getGenre()),
                    newFilm.getMpa().getId(),
                    null,
                    newFilm.getId()
            );
        }

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

    @Override
    public List<Film> getFilmsByDirectorIdSortByYear(int directorId) {
        log.info("Отправка запроса GET_FILMS_BY_DIRECTOR_ID_SORT_BY_YEAR");
        return jdbc.query(Query.GET_FILMS_BY_DIRECTOR_ID_SORT_BY_YEAR.getQuery(), mapperFilm,
                String.valueOf(directorId));
    }

    @Override
    public List<Film> getFilmsByDirectorIdSortByLikes(int directorId) {
        log.info("Отправка запроса GET_FILMS_BY_DIRECTOR_ID_SORT_BY_LIKES");
        return jdbc.query(Query.GET_FILMS_BY_DIRECTOR_ID_SORT_BY_LIKES.getQuery(), mapperFilm,
                String.valueOf(directorId));
    }

    @Override
    public List<Film> getPopularFilmsByTitle(String query) {
        log.info("Отправка запроса FIND_POPULAR_FILMS_BY_TITLE");
        return jdbc.query(Query.FIND_POPULAR_FILMS_BY_TITLE.getQuery(), mapperFilm, getSearchString(query));
    }

    @Override
    public List<Film> getPopularFilmsByDirector(String query) {
        log.info("Отправка запроса FIND_DIRECTOR_LIST_BY_NAME");
        List<Director> directorList = jdbc.query(Query.FIND_DIRECTOR_LIST_BY_NAME.getQuery(),
                directorRowMapper,
                getSearchString(query));
        List<Film> filmList = new ArrayList<>();

        directorList.forEach(director -> filmList.addAll(jdbc.query(
                Query.GET_ALL_FILMS_BY_DIRECTOR_ID_SORT_BY_LIKES.getQuery(),
                mapperFilm,
                String.valueOf(director.getId()))));

        return filmList;
    }

    @Override
    public List<Film> getPopularFilmsByTitleAndDirector(String query) {
        log.info("Отправка запроса FIND_POPULAR_FILMS_BY_TITLE_AND_DIRECTOR");
        return jdbc.query(Query.FIND_POPULAR_FILMS_BY_TITLE_AND_DIRECTOR.getQuery(),
                mapperFilm,
                getSearchString(query),
                getSearchString(query));
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

    private String convertDirectorsToString(List<Director> directors) {
        StringBuilder stringBuilder = new StringBuilder();
        String prefix = "";

        if (directors != null) {
            for (Director director : directors) {
                stringBuilder.append(prefix);
                prefix = ", ";
                stringBuilder.append(director.getId());
            }
        }

        return stringBuilder.toString();
    }

    private static String getSearchString(String query) {
        return MessageFormat.format("%{0}%", query);
    }
}
