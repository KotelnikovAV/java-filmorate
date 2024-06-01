package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryFilmStorageTest {
    private static final FilmStorage filmStorage = new InMemoryFilmStorage();;

    @BeforeAll
    public static void beforeAll() {
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
        filmStorage.create(film1);
        filmStorage.create(film2);
        filmStorage.create(film3);
    }

    @Test
    void checkFindAll() {
        assertEquals(3, filmStorage.findAll().size(), "Ошибка в получении списка всех фильмов");
    }

    @Test
    void checkCreateNotValidLocalDateFilm() {
        Film film4 = Film.builder()
                .name("Терминатор 2")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1580, 1, 1))
                .duration(140)
                .build();
        assertThrows(ValidationException.class, () -> filmStorage.create(film4), "Не проходит валидация " +
                "фильма с неправильной датой");
    }

    @Test
    void checkUpdate() {
        Film film4 = Film.builder()
                .id(3)
                .name("Звезда")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        filmStorage.update(film4);
        assertEquals("Звезда", filmStorage.getFilm(3).getName(), "Фильм не обновился");
    }

    @Test
    void checkUpdateNotValidLocalDateFilm() {
        Film film4 = Film.builder()
                .id(3)
                .name("Звезда")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1580, 1, 1))
                .duration(140)
                .build();
        assertThrows(ValidationException.class, () -> filmStorage.update(film4), "Не проходит валидация " +
                "фильма с неправильной датой");
    }

    @Test
    void checkUpdateNotFoundedFilm() {
        Film film4 = Film.builder()
                .id(6)
                .name("Звезда")
                .description("Про терминатора")
                .releaseDate(LocalDate.of(1980, 1, 1))
                .duration(140)
                .build();
        assertThrows(NotFoundException.class, () -> filmStorage.update(film4), "Произошло обновление " +
                "несуществующего фильма");
    }

    @Test
    void checkGetFilm() {
        assertEquals("Терминатор", filmStorage.getFilm(1).getName(), "Был возвращен " +
                "неправильный фильм");
    }

    @Test
    void checkGetNotFoundedFilm() {
        assertThrows(NotFoundException.class, () -> filmStorage.getFilm(4), "Был возвращен " +
                "несуществующий фильм");
    }
}