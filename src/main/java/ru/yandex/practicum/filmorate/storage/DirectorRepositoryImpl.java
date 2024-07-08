package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DirectorRepositoryImpl implements DirectorRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<Director> mapperDirector;

    @Override
    public List<Director> getAllDirectors() {
        log.info("Отправка запроса GET_ALL_DIRECTORS");
        return jdbc.query(Query.GET_ALL_DIRECTORS.getQuery(), mapperDirector);
    }

    @Override
    public Director getDirector(int directorId) {
        log.info("Отправка запроса GET_DIRECTOR_BY_ID");
        checkDirector(directorId);
        return jdbc.queryForObject(Query.GET_DIRECTOR_BY_ID.getQuery(), mapperDirector, directorId);
    }

    @Override
    public Director create(Director director) {
        log.info("Отправка запроса INSERT_DIRECTOR");
        int id = BaseDbStorage.insert(jdbc, Query.INSERT_DIRECTOR.getQuery(), director.getName());
        director.setId(id);
        return director;
    }

    @Override
    public Director update(Director newDirector) {
        log.info("Отправка запроса UPDATE_DIRECTOR");
        int rowsUpdated = jdbc.update(Query.UPDATE_DIRECTOR.getQuery(), newDirector.getName(), newDirector.getId());

        if (rowsUpdated == 0) {
            throw new NotFoundException("Такого режиссёра нет");
        }

        return newDirector;
    }

    @Override
    public void delete(int directorId) {
        log.info("Отправка запроса UPDATE_FILM_BY_DELETE_DIRECTOR");
        int rowsUpdated = jdbc.update(Query.UPDATE_FILM_BY_DELETE_DIRECTOR.getQuery(), String.valueOf(directorId));

        if (rowsUpdated == 0) {
            log.info("Таблица с фильмами не обновилась, так как в ней нет режиссёра с id = " + directorId);
        } else {
            log.info("Таблица с фильмами была обновлена, вместо director_id было установлено значение NULL");
        }

        log.info("Отправка запроса DELETE_DIRECTOR");
        rowsUpdated = jdbc.update(Query.DELETE_DIRECTOR.getQuery(), directorId);

        if (rowsUpdated == 0) {
            throw new NotFoundException("Такого режиссёра нет");
        }
    }

    @Override
    public void checkDirector(int directorId) {
        log.info("Отправка запроса CHECK_DIRECTOR");
        Optional<Integer> countDirectors = Optional.ofNullable(jdbc.queryForObject(Query.CHECK_DIRECTOR.getQuery(),
                Integer.class, directorId));

        if (countDirectors.isEmpty()) {
            throw new InternalServerException("Ошибка проверки наличия режиссёра");
        } else if (countDirectors.get() == 0) {
            throw new NotFoundException("Такого режиссёра нет");
        }
    }

    @Override
    public List<Director> getListDirector(String listId) {
        log.info("Начало процесса получения списка жанров для фильма");
        List<Director> directors = new ArrayList<>();

        if (listId != null && !listId.isEmpty()) {
            String[] idDirectors = listId.split(", ");
            for (String idDirector : idDirectors) {
                directors.add(getDirector(Integer.parseInt(idDirector)));
            }
        }

        return directors;
    }
}
