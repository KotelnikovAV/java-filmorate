package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    FilmDto addLike(int filmId, int userId);

    FilmDto deleteLike(int filmId, int userId);

    List<FilmDto> getFilms(int count);

    Collection<FilmDto> findAll();

    FilmDto create(FilmDto film);

    FilmDto update(FilmDto newFilm);

    FilmDto getFilm(int filmId);
}