package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@UtilityClass
@Slf4j
public class UserMapper {

    public User mapToUser(UserDto userDto) {
        log.info("Начало преобразования UserDto в User");

        if (userDto == null) {
            return null;
        }

        User user = User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .login(userDto.getLogin())
                .name(userDto.getName())
                .birthday(userDto.getBirthday())
                .build();
        log.info("Преобразование UserDto в User успешно завершено");
        return user;
    }

    public UserDto mapToUserDto(User user) {
        log.info("Начало преобразования User в UserDto");

        if (user == null) {
            return null;
        }

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
        log.info("Преобразование User в UserDto успешно завершено");
        return userDto;
    }
}