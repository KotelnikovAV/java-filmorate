package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {

    public User mapToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .login(userDto.getLogin())
                .name(userDto.getName())
                .birthday(userDto.getBirthday())
                .build();
    }

    public UserDto mapToUserDto(User user, List<User> friends) {
        List<Integer> idFriends = new ArrayList<>();
        if (friends != null) {
            idFriends = friends
                    .stream()
                    .map(User::getId)
                    .toList();
        }
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .friends(idFriends)
                .build();
    }
}
