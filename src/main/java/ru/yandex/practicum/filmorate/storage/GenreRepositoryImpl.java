package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GenreRepositoryImpl implements GenreRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<Genre> mapperGenre;

    @Override
    public List<Genre> getAllGenres() {
        log.info("Отправка запроса GET_ALL_GENRE");
        return jdbc.query(Query.GET_ALL_GENRE.getQuery(), mapperGenre);
    }

    @Override
    public Genre getGenre(int id) {
        log.info("Отправка запроса GET_GENRE_BY_ID");
        checkGenre(id);
        return jdbc.queryForObject(Query.GET_GENRE_BY_ID.getQuery(), mapperGenre, id);
    }

    @Override
    public void checkGenre(int id) throws NotFoundException {
        log.info("Отправка запроса CHECK_GENRE");
        Optional<Integer> countGenre = Optional.ofNullable(jdbc.queryForObject(Query.CHECK_GENRE.getQuery(),
                Integer.class, id));
        if (countGenre.isEmpty()) {
            throw new InternalServerException("Ошибка добавления в друзья");
        } else if (countGenre.get() == 0) {
            throw new NotFoundException("Такого жанра нет");
        }
    }

    @Override
    public List<Genre> getListGenre(String listId) {
        log.info("Начало процесса получения списка жанров для фильма");
        List<Genre> genres = new ArrayList<>();

        if (listId != null && !listId.isEmpty()) {
            String[] idGenres = listId.split(", ");
            for (String idGenre : idGenres) {
                genres.add(getGenre(Integer.parseInt(idGenre)));
            }
        }

        return genres;
    }
}