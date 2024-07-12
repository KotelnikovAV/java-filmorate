package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@UtilityClass
@Slf4j
public class MpaMapper {

    public Mpa mapToMpa(MpaDto mpaDto) {
        log.info("Начало преобразования MpaDto в Mpa");

        if (mpaDto == null) {
            return null;
        }

        Mpa mpa = Mpa.builder()
                .id(mpaDto.getId())
                .build();
        log.info("Преобразование MpaDto в Mpa успешно завершено");
        return mpa;
    }

    public MpaDto mapToMpaDto(Mpa mpa) {
        log.info("Начало преобразования Mpa в MpaDto");

        if (mpa == null) {
            return null;
        }

        MpaDto mpaDto = MpaDto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
        log.info("Преобразование Mpa в MpaDto успешно завершено");
        return mpaDto;
    }
}
