package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getAllGenres() {
        log.info("Начало процесса получения всех жанров");
        return genreRepository.getAllGenres();
    }

    @Override
    public Genre getGenre(int id) {
        log.info("Начало процесса получения жанра по id = " + id);
        return genreRepository.getGenre(id);
    }
}
