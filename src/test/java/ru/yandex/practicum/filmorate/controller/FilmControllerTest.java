package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceImp;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    FilmController filmController;
    @Autowired
    MockMvc mockMvc;

    /*@BeforeAll
    public static void beforeAll() throws Exception {
        User user1 = User.builder()
                .name("Андрей")
                .login("andrey1")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        User user2 = User.builder()
                .name("Юля")
                .login("andrey1")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        User user3 = User.builder()
                .name("Иван")
                .login("andrey1")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        User user4 = User.builder()
                .name("Артем")
                .login("andrey1")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        Film film1 = Film.builder()
                .name("Терминатор")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        Film film2 = Film.builder()
                .name("Терминатор 2")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        Film film3 = Film.builder()
                .name("Терминатор 3")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        mockMvc.perform(post("/films")
                .content(mapper.writeValueAsString(film1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"));
        mockMvc.perform(post("/films")
                .content(mapper.writeValueAsString(film2))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"));
        mockMvc.perform(post("/films")
                .content(mapper.writeValueAsString(film3))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"));
    }*/

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
    public void checkCreateNotValidLocalDateFilm() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmServiceImp(filmStorage, userStorage);
        FilmController filmController1 = new FilmController(filmService);
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

    @Test
    public void checkUpdateNotValidIdFilm() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmServiceImp(filmStorage, userStorage);
        FilmController filmController1 = new FilmController(filmService);
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

    @Test
    public void check() throws Exception {
        Film film1 = Film.builder()
                .name("Терминатор")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        Film film2 = Film.builder()
                .name("Терминатор 2")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        Film film3 = Film.builder()
                .name("Терминатор 3")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        mockMvc.perform(post("/films")
                .content(mapper.writeValueAsString(film1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"));
        mockMvc.perform(post("/films")
                .content(mapper.writeValueAsString(film2))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"));
        mockMvc.perform(post("/films")
                .content(mapper.writeValueAsString(film3))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"));
        String a = mockMvc.perform(get("/films"))
                .andReturn().getResponse().getContentAsString();
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(Collection.class, Film.class);
        Collection<Film> articles = mapper.readValue(a, collectionType);
    }
}