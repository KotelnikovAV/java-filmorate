package ru.yandex.practicum.filmorate.storage.indb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Query;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilmRepositoryImp implements FilmRepository {

    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapperFilm;

    @Override
    public Collection<Film> findAll() {
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
                film.getGenre(),
                film.getMpa()
        );
        film.setId(id);
        log.info("Фильм добавлен с id = " + id);
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
                newFilm.getGenre(),
                newFilm.getMpa(),
                newFilm.getId()
        );

        if (rowsUpdated == 0) {
            throw new NotFoundException("Такого фильма нет");
        }

        log.info("Фильм обновлен");
        return newFilm;
    }

    @Override
    public Film getFilm(int filmId) {
        log.info("Отправка запроса FIND_FILM_BY_ID");
        return jdbc.queryForObject(Query.FIND_FILM_BY_ID.getQuery(), mapperFilm, filmId);
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        log.info("Отправка запроса FIND_POPULAR_FILMS");
        List<Film> film = jdbc.query(Query.FIND_POPULAR_FILMS.getQuery(), mapperFilm, count);
        return film
                .stream()
                .peek(film1 -> film1.setLikes(getListLikes(film1)))
                .toList();
    }

    @Override
    public Film addLike(int filmId, int userId) {
        log.info("Начало проверки наличия лайка");
        Optional<Integer> count = Optional.ofNullable(jdbc.queryForObject(Query.CHECKING_AVAILABILITY_FILM.getQuery(),
                Integer.class, filmId, userId));

        if (count.isEmpty()) {
            throw new InternalServerException("Не удалось поставить лайк");
        }

        if (count.get() > 0) {
            throw new InternalServerException("Данный пользователь уже поставил лайк");
        }

        log.info("Отправка запроса ADD_LIKE");
        int rowsCreated = jdbc.update(Query.ADD_LIKE.getQuery(), filmId, userId);

        if (rowsCreated == 0) {
            throw new InternalServerException("Не удалось поставить лайк");
        }

        List<Integer> likesFilm = jdbc.queryForList(Query.GET_LIKES.getQuery(), Integer.class, filmId);
        Film film = getFilm(filmId);
        film.setLikes(likesFilm);
        log.info("Лайк поставлен");
        return film;
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        log.info("Отправка запроса DELETE_LIKE");
        int rowsDeleted = jdbc.update(Query.DELETE_LIKE.getQuery(), userId);

        if (rowsDeleted == 0) {
            throw new InternalServerException("Данный пользователь лайк не ставил");
        }

        List<Integer> likesFilm = jdbc.queryForList(Query.GET_LIKES.getQuery(), Integer.class, filmId);
        Film film = getFilm(filmId);
        film.setLikes(likesFilm);
        log.info("Лайк удален");
        return film;
    }

    private List<Integer> getListLikes(Film film) {
        log.info("Отправка запроса FIND_LIST_LIKES");
        return jdbc.queryForList(Query.FIND_LIST_LIKES.getQuery(), Integer.class, film.getId());
    }
}
