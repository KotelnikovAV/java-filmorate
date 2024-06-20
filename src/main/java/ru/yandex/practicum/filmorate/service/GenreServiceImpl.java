package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ResponseGenreDto;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<ResponseGenreDto> getAllGenres() {
        log.info("Начало процесса получения всех жанров");
        List<Genre> genres = genreRepository.getAllGenres();
        return genres
                .stream()
                .map(GenreMapper::mapToResponseGenreDto)
                .toList();
    }

    @Override
    public ResponseGenreDto getGenre(int id) {
        log.info("Начало процесса получения жанра по id = " + id);
        return GenreMapper.mapToResponseGenreDto(genreRepository.getGenre(id));
    }
}
