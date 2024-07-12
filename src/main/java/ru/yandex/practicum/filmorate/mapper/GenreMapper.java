package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;


@UtilityClass
@Slf4j
public class GenreMapper {

    public Genre mapToGenre(GenreDto genreDto) {
        log.info("Начало преобразования GenreDto в Genre");

        if (genreDto == null) {
            return null;
        }

        Genre genre = Genre.builder()
                .id(genreDto.getId())
                .build();
        log.info("Преобразование GenreDto в Genre успешно завершено");
        return genre;
    }

    public GenreDto mapToGenreDto(Genre genre) {
        log.info("Начало преобразования Genre в GenreDto");

        if (genre == null) {
            return null;
        }

        GenreDto genreDto = GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
        log.info("Преобразование Genre в GenreDto успешно завершено");
        return genreDto;
    }
}
