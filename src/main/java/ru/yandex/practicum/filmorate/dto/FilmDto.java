package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class FilmDto {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private List<Genre> genres;
    private Mpa mpa;
    private List<Integer> likes;
}