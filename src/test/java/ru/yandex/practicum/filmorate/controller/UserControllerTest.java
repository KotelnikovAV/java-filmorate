package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserServiceImp;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserController userController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkCreateValidUser() throws Exception {
        User user1 = User.builder()
                .name("Андрей")
                .login("andrey1")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        this.mockMvc.perform(post("/users")
                .content(mapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void checkCreateNotValidEmailUser() throws Exception {
        User user1 = User.builder()
                .name("Андрей")
                .login("qwer")
                .email("это-неправильный?эмейл@")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        this.mockMvc.perform(post("/users")
                .content(mapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().is4xxClientError());
    }

    @Test
    public void checkCreateNotValidLoginUser() throws Exception {
        User user1 = User.builder()
                .name("Андрей")
                .login("")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        User user2 = User.builder()
                .name("Андрей")
                .login("wef 12 fwe")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        this.mockMvc.perform(post("/users")
                .content(mapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().is4xxClientError());
    }

    @Test
    public void checkCreateNotValidBirthdayUser() throws Exception {
        User user1 = User.builder()
                .name("Андрей")
                .login("qwer")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(2200, 10, 20))
                .build();
        this.mockMvc.perform(post("/users")
                .content(mapper.writeValueAsString(user1))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().is4xxClientError());
    }

    @Test
    public void checkUpdateNotValidIdFilm() {
        UserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserServiceImp(userStorage);
        UserController userController1 = new UserController(userService);
        User user1 = User.builder()
                .id(4)
                .name("Андрей")
                .login("qwer")
                .email("qwer@ya.ru")
                .birthday(LocalDate.of(1996, 10, 20))
                .build();
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> userController1.update(user1));
        assertEquals("Пользователя с id = 4 не существует", thrown.getMessage());
    }
}