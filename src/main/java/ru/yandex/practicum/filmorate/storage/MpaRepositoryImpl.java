package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Query;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MpaRepositoryImpl implements MpaRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<Mpa> mapperMpa;

    @Override
    public List<Mpa> getAllMpa() {
        log.info("Отправка запроса GET_ALL_MPA");
        return jdbc.query(Query.GET_ALL_MPA.getQuery(), mapperMpa);
    }

    @Override
    public Mpa getMpa(int id) {
        log.info("Отправка запроса GET_MPA_BY_ID");
        checkMpa(id);
        return jdbc.queryForObject(Query.GET_MPA_BY_ID.getQuery(), mapperMpa, id);
    }

    @Override
    public void checkMpa(int id) throws NotFoundException {
        log.info("Отправка запроса CHECK_MPA");
        Optional<Integer> countMpa = Optional.ofNullable(jdbc.queryForObject(Query.CHECK_MPA.getQuery(),
                Integer.class, id));

        if (countMpa.isEmpty()) {
            throw new InternalServerException("Ошибка проверки наличия рейтинга");
        } else if (countMpa.get() == 0) {
            throw new NotFoundException("Такого рейтинга нет");
        }
    }
}
