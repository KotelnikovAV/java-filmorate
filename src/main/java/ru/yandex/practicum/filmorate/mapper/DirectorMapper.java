package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;

@UtilityClass
@Slf4j
public class DirectorMapper {

    public Director mapToDirector(DirectorDto directorDto) {
        log.info("Начало преобразования DirectorDto в Director");

        if (directorDto == null) {
            return null;
        }

        Director director = Director.builder()
                .id(directorDto.getId())
                .name(directorDto.getName())
                .build();
        log.info("Преобразование DirectorDto в Director успешно завершено");
        return director;
    }

    public DirectorDto mapToDirectorDto(Director director) {
        log.info("Начало преобразования Director в DirectorDto");

        if (director == null) {
            return null;
        }

        DirectorDto directorDto = DirectorDto.builder()
                .id(director.getId())
                .name(director.getName())
                .build();
        log.info("Преобразование Director в DirectorDto успешно завершено");
        return directorDto;
    }
}