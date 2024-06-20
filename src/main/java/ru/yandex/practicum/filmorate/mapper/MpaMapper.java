package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.RequestMpaDto;
import ru.yandex.practicum.filmorate.dto.ResponseMpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@UtilityClass
public class MpaMapper {

    public Mpa mapToMpa(RequestMpaDto requestMpaDto) {
        return Mpa.builder()
                .id(requestMpaDto.getId())
                .build();
    }

    public ResponseMpaDto mapToResponseMpaDto(Mpa mpa) {
        return ResponseMpaDto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }
}
