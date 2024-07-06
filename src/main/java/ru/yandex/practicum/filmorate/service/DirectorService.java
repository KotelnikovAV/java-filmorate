package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.DirectorDto;

import java.util.List;

public interface DirectorService {

    List<DirectorDto> getAllDirectors();

    DirectorDto getDirector(int directorId);

    DirectorDto create(DirectorDto director);

    DirectorDto update(DirectorDto newDirector);

    void delete(int directorId);
}
