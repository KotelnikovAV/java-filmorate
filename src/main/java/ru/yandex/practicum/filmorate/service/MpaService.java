package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.ResponseMpaDto;

import java.util.List;

public interface MpaService {

    List<ResponseMpaDto> getAllMpa();

    ResponseMpaDto getMpa(int id);
}
