package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"id"})
@Builder
public class User {
    private long id;
    @NotNull
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "имя пользователя должно быть без специальных символов и пробелов")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private List<Long> friends;
}