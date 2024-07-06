package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {
    private final DirectorRepository directorRepository;

    @Override
    public List<DirectorDto> getAllDirectors() {
        log.info("Начало процесса получения всех режиссёров");
        List<Director> directors = directorRepository.getAllDirectors();
        log.info("Список всех режиссёров получен");
        return directors.stream()
                .map(DirectorMapper::mapToDirectorDto)
                .toList();
    }

    @Override
    public DirectorDto getDirector(int directorId) {
        log.info("Начало процесса получения режиссёра по id = {}", directorId);
        Director director = directorRepository.getDirector(directorId);
        log.info("Режиссёр получен");
        return DirectorMapper.mapToDirectorDto(director);
    }

    @Override
    public DirectorDto create(DirectorDto director) {
        log.info("Начало процесса создания режиссёра");
        Director createdDirector = directorRepository.create(DirectorMapper.mapToDirector(director));
        log.info("Режиссёр создан");
        return DirectorMapper.mapToDirectorDto(createdDirector);
    }

    @Override
    public DirectorDto update(DirectorDto newDirector) {
        log.info("Начало процесса обновления режиссёра");
        Director director = directorRepository.update(DirectorMapper.mapToDirector(newDirector));
        log.info("Режиссёр обновлен");
        return DirectorMapper.mapToDirectorDto(director);
    }

    @Override
    public void delete(int directorId) {
        log.info("Начало процесса удаления режиссёра");
        directorRepository.delete(directorId);
        log.info("Режиссёр удален");
    }
}
