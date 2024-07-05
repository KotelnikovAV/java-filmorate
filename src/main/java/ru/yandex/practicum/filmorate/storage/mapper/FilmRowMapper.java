package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DirectorRepository;
import ru.yandex.practicum.filmorate.storage.GenreRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final DirectorRepository directorRepository;
    private final GenreRepository genreRepository;

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate((resultSet.getTimestamp("releaseDate")).toLocalDateTime().toLocalDate())
                .duration(resultSet.getInt("duration"))
                .genre(genreRepository.getListGenre(resultSet.getString("genre")))
                .mpa(
                        Mpa.builder()
                                .id(resultSet.getInt("mpa_id"))
                                .name(resultSet.getString("mpa_name"))
                                .build()
                )
                .directors(directorRepository.getListDirector(resultSet.getString("directors")))
                .build();
    }
}