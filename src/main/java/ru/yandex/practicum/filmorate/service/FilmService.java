package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;

import java.util.List;

public interface FilmService {

    FilmDto addLike(int filmId, int userId);

    FilmDto deleteLike(int filmId, int userId);

    List<FilmDto> getPopularFilms(int count);

    List<FilmDto> findAll();

    FilmDto create(FilmDto film);

    FilmDto update(FilmDto newFilm);

    FilmDto getFilmById(int filmId);

    List<FilmDto> getFilmsByDirectorId(int directorId, String sortBy);
}