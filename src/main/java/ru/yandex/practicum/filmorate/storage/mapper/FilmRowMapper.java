package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate((resultSet.getTimestamp("releaseDate")).toLocalDateTime().toLocalDate())
                .duration(resultSet.getInt("duration"))
                .genre(resultSet.getString("genre"))
                .mpa(resultSet.getInt("mpa_id"))
                .build();
    }
}