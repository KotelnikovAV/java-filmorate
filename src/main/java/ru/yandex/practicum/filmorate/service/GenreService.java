package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.ResponseGenreDto;

import java.util.List;

public interface GenreService {

    List<ResponseGenreDto> getAllGenres();

    ResponseGenreDto getGenre(int id);
}