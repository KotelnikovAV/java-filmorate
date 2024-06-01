package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class FilmControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    FilmService filmService;
    @MockBean
    UserService userService;

    @Test
    public void checkCreateValidFilm() throws Exception {
        Film film1 = Film.builder()
                .name("Терминатор")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        mockMvc.perform(post("/films")
                .content(mapper.writeValueAsString(film1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void checkCreateNotValidNameFilm() throws Exception {
        Film film1 = Film.builder()
                .name("")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        mockMvc.perform(post("/films")
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
        mockMvc.perform(post("/films")
                        .content(mapper.writeValueAsString(film1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")).andExpect(status().is4xxClientError());
    }

    @Test
    public void checkCreateNotValidDescriptionFilm() throws Exception {
        Film film1 = Film.builder()
                .name("Терминатор 2")
                .description("5!7@$#9s6Gt^4D*2+hQlp~xoV0w3FjYbu1cEznC&L8K-WqrRiXvUJMyfdkPZTaIBOmNgH5!7@$#9s6Gt^4D*2" +
                        "+hQlp~xoV0w3FjYbu1cEznC&L8K-WqrRiXvUJMyfdkPZTaIBOmNgH5!7@$#9s6Gt^4D*2+hQlp~xoV0w3FjYbu1cEz" +
                        "nC&L8K-WqrRiXvUJMyfdkPZTаa")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        mockMvc.perform(post("/films")
                .content(mapper.writeValueAsString(film1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().is4xxClientError());
    }
}