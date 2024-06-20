package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.RequestGenreDto;
import ru.yandex.practicum.filmorate.dto.ResponseGenreDto;
import ru.yandex.practicum.filmorate.model.Genre;


@UtilityClass
public class GenreMapper {

    public Genre mapToGenre(RequestGenreDto requestGenreDto) {
        return Genre.builder()
                .id(requestGenreDto.getId())
                .build();
    }

    public ResponseGenreDto mapToResponseGenreDto(Genre genre) {
        return ResponseGenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
