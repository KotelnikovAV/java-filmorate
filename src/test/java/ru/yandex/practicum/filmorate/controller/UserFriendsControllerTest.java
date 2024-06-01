package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserFriendsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    FilmService filmService;
    @MockBean
    UserService userService;

    @Test
    void checkGetAllFriends() throws Exception {
        mockMvc.perform(get("/users/1/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void checkGetMutualFriends() throws Exception {
        mockMvc.perform(get("/users/1/friends/common/2")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void addFriend() throws Exception {
        mockMvc.perform(put("/users/1/friends/2")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteFriend() throws Exception {
        mockMvc.perform(delete("/users/1/friends/2")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")).andExpect(status().is2xxSuccessful());
    }
}