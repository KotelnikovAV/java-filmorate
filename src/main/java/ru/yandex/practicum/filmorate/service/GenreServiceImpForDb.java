package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.indb.GenreRepository;

import java.util.List;

@Service
@Slf4j
public class GenreServiceImpForDb implements GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpForDb(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

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
