package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;

import java.util.List;

public interface FilmService {

    ResponseFilmDto addLike(int filmId, int userId);

    ResponseFilmDto deleteLike(int filmId, int userId);

    List<ResponseFilmDto> getPopularFilms(int count);

    List<ResponseFilmDto> findAll();

    ResponseFilmDto create(RequestFilmDto film);

    ResponseFilmDto update(RequestFilmDto newFilm);

    ResponseFilmDto getFilmById(int filmId);
}