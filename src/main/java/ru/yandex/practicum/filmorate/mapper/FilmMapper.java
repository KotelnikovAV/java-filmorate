package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class FilmMapper {

    public Film mapToFilm(FilmDto filmDto) {
        log.info("Начало преобразования FilmDto в Film");
        LinkedHashSet<GenreDto> requestGenreDto = filmDto.getGenres();
        List<Genre> genres = new ArrayList<>();

        if (requestGenreDto != null) {
            genres = requestGenreDto.stream()
                    .map(GenreMapper::mapToGenre)
                    .toList();
        }
        Film film =  Film.builder()
                .id(filmDto.getId())
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .releaseDate(filmDto.getReleaseDate())
                .duration(filmDto.getDuration())
                .genre(genres)
                .mpa(MpaMapper.mapToMpa(filmDto.getMpa()))
                .build();
        log.info("Преобразование FilmDto в Film успешно завершено");
        return film;
    }

    public FilmDto mapToFilmDto(Film film) {
        log.info("Начало преобразования Film в FilmDto");
        List<Genre> genres = film.getGenre();
        LinkedHashSet<GenreDto> responseGenreDto = new LinkedHashSet<>();

        if (genres != null) {
            responseGenreDto = genres.stream()
                    .map(GenreMapper::mapToGenreDto)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        FilmDto filmDto = FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(responseGenreDto)
                .mpa(MpaMapper.mapToMpaDto(film.getMpa()))
                .build();
        log.info("Преобразование Film в FilmDto успешно завершено");
        return filmDto;
    }
}