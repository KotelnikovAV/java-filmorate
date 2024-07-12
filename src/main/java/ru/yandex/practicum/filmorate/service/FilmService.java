package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.FilmDto;

import java.util.List;

public interface FilmService {

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<FilmDto> getPopularFilms(int count, int genreId, int year);

    List<FilmDto> findAll();

    FilmDto create(FilmDto film);

    FilmDto update(FilmDto newFilm);

    FilmDto getFilmById(int filmId);

    List<FilmDto> getCommonFilms(int userId, int friendId);

    void delete(int filmId);

    List<FilmDto> getFilmsByDirectorId(int directorId, String sortBy);

    List<FilmDto> getPopularFilmsBySearchParam(String query, List<String> searchParams);
}