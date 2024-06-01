package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryUserStorageTest {
    private static final UserStorage userStorage = new InMemoryUserStorage();

    @BeforeAll
    public static void beforeAll() {
        User user1 = User.builder()
                .name("Андрей")
                .login("andrey1")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        User user2 = User.builder()
                .name("Маша")
                .login("andrey1")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        User user3 = User.builder()
                .name("Булат")
                .login("andrey1")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        userStorage.create(user1);
        userStorage.create(user2);
        userStorage.create(user3);
    }

    @Test
    public void checkUpdate() {
        User user4 = User.builder()
                .id(3)
                .name("Юля")
                .login("andrey1")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        assertEquals("Юля", userStorage.update(user4).getName(), "Пользователь не обновился");
    }

    @Test
    public void checkUpdateNotFoundedUser() {
        User user4 = User.builder()
                .id(5)
                .name("Юля")
                .login("andrey1")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        assertThrows(NotFoundException.class, () -> userStorage.update(user4));
    }

    @Test
    public void checkGetUser() {
        assertEquals("Андрей", userStorage.getUser(1).getName(), "Был возвращен неправильный " +
                "пользователь");
    }

    @Test
    public void checkGetNotFoundedUser() {
        assertThrows(NotFoundException.class, () -> userStorage.getUser(543), "Был возвращен " +
                "несуществующий пользователь");
    }

    @Test
    public void checkFindAll() {
        assertEquals(3, userStorage.findAll().size(), "Были возвращены не все пользователи");
    }
}