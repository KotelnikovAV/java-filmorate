package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserDto {
    private int id;
    @NotNull
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "имя пользователя должно быть без специальных символов и пробелов")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private List<Integer> friends;
}