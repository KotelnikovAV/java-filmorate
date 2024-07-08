package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaRepository mpaRepository;

    @Override
    public List<MpaDto> getAllMpa() {
        log.info("Начало процесса получения всех рейтингов");
        List<Mpa> mpa = mpaRepository.getAllMpa();
        log.info("Список всех рейтингов получен");
        return mpa.stream()
                .map(MpaMapper::mapToMpaDto)
                .toList();
    }

    @Override
    public MpaDto getMpa(int id) {
        log.info("Начало процесса получения рейтинга по id = " + id);
        Mpa mpa = mpaRepository.getMpa(id);
        log.info("Рейтинг получен");
        return MpaMapper.mapToMpaDto(mpa);
    }
}
