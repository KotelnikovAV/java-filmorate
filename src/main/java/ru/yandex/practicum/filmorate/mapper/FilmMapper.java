package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@NoArgsConstructor
public class FilmMapper {

    public static Film mapToFilm(FilmDto filmDto) {
        List<Genre> idGenres = filmDto.getGenres();
        StringBuilder genre = new StringBuilder();
        String prefix = "";

        if (idGenres != null) {
            for (Genre idGenre : idGenres) {
                genre.append(prefix);
                prefix = ", ";
                genre.append(idGenre.getId());
            }
        }

        return Film.builder()
                .id(filmDto.getId())
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .releaseDate(filmDto.getReleaseDate())
                .duration(filmDto.getDuration())
                .genre(genre.toString())
                .mpa(filmDto.getMpa().getId())
                .build();
    }

    public static FilmDto mapToFilmDto(Film film, List<Genre> genres, Mpa mpa) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(genres)
                .mpa(mpa)
                .likes(film.getLikes())
                .build();
    }
}