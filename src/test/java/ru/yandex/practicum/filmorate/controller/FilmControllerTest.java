package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
class FilmControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    FilmController filmController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkCreateValidFilm() throws Exception {
        Film film1 = Film.builder()
                .name("Терминатор")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        this.mockMvc.perform(post("/films")
                .content(mapper.writeValueAsString(film1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().isOk());
    }

    @Test
    public void checkCreateNotValidNameFilm() throws Exception {
        Film film1 = Film.builder()
                .name("")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        this.mockMvc.perform(post("/films")
                        .content(mapper.writeValueAsString(film1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")).andExpect(status().is4xxClientError());
    }

    @Test
    public void checkCreateNotValidDurationFilm() throws Exception {
        Film film1 = Film.builder()
                .name("Терминатор")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(-140)
                .build();
        this.mockMvc.perform(post("/films")
                        .content(mapper.writeValueAsString(film1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")).andExpect(status().is4xxClientError());
    }

    @Test
    public void checkCreateNotValidLocalDateFilm() {
        FilmController filmController1 = new FilmController();
        Film film1 = Film.builder()
                .name("Терминатор 2")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1580, 1, 1))
                .duration(140)
                .build();
        ValidationException thrown = assertThrows(ValidationException.class, () -> filmController1.create(film1));
        assertEquals("Дата релиза фильма должна быть не раньше 28 декабря 1895 г.",
                thrown.getMessage());
    }

    @Test
    public void checkCreateNotValidDescriptionFilm() {
        FilmController filmController1 = new FilmController();
        Film film1 = Film.builder()
                .name("Терминатор 2")
                .description("5!7@$#9s6Gt^4D*2+hQlp~xoV0w3FjYbu1cEznC&L8K-WqrRiXvUJMyfdkPZTaIBOmNgH5!7@$#9s6Gt^4D*2" +
                        "+hQlp~xoV0w3FjYbu1cEznC&L8K-WqrRiXvUJMyfdkPZTaIBOmNgH5!7@$#9s6Gt^4D*2+hQlp~xoV0w3FjYbu1cEz" +
                        "nC&L8K-WqrRiXvUJMyfdkPZTаa")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        ValidationException thrown = assertThrows(ValidationException.class, () -> filmController1.create(film1));
        assertEquals("Описание должно содержать меньше 200 символов",
                thrown.getMessage());
    }

    @Test
    public void checkUpdateNotValidIdFilm() {
        FilmController filmController1 = new FilmController();
        Film film1 = Film.builder()
                .id(4)
                .name("Человек паук 2")
                .description("Про человека паука")
                .releaseDate(LocalDate.of(2010, 1, 1))
                .duration(160)
                .build();
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> filmController1.update(film1));
        assertEquals("Фильма с id = 4 не существует",
                thrown.getMessage());
    }
}