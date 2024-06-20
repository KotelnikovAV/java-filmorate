package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.RequestGenreDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.dto.ResponseGenreDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@UtilityClass
public class FilmMapper {

    public Film mapToFilm(RequestFilmDto requestFilmDto) {
        LinkedHashSet<RequestGenreDto> requestGenreDto = requestFilmDto.getGenres();
        List<Genre> genres = new ArrayList<>();

        if (requestGenreDto != null) {
            genres = requestGenreDto
                    .stream()
                    .map(GenreMapper::mapToGenre)
                    .toList();
        }

        return Film.builder()
                .id(requestFilmDto.getId())
                .name(requestFilmDto.getName())
                .description(requestFilmDto.getDescription())
                .releaseDate(requestFilmDto.getReleaseDate())
                .duration(requestFilmDto.getDuration())
                .genre(genres)
                .mpa(MpaMapper.mapToMpa(requestFilmDto.getMpa()))
                .build();
    }

    public ResponseFilmDto mapToResponseFilmDto(Film film, List<Integer> likes) {
        List<Genre> genres = film.getGenre();
        List<ResponseGenreDto> responseGenreDto = new ArrayList<>();

        if (genres != null) {
            responseGenreDto = genres.stream()
                    .map(GenreMapper::mapToResponseGenreDto)
                    .toList();
        }

        return ResponseFilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(responseGenreDto)
                .mpa(MpaMapper.mapToResponseMpaDto(film.getMpa()))
                .likes(likes)
                .build();
    }
}