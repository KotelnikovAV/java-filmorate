package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Query;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FriendsRepositoryImpl implements FriendsRepository {
    private final JdbcTemplate jdbc;
    private final UserRepository userRepository;

    @Override
    public List<User> getAllFriendsById(int id) {
        log.info("Отправка запроса FIND_FRIEND");
        checkAndReturnUser(id);
        List<Integer> idFriends = jdbc.queryForList(Query.FIND_FRIEND.getQuery(), Integer.class, id, id);
        return idFriends
                .stream()
                .map(this::checkAndReturnUser)
                .toList();
    }

    @Override
    public User addFriend(int id, int friendId) {
        checkAndReturnUser(id);
        checkAndReturnUser(friendId);
        log.info("Начало проверки на наличие ранее отправленной заявки");
        Optional<Integer> count1 = Optional.ofNullable(jdbc.queryForObject(Query.CHECKING_AVAILABILITY_USER.getQuery(),
                Integer.class, id, friendId));
        Optional<Integer> count2 = Optional.ofNullable(jdbc.queryForObject(Query.CHECKING_AVAILABILITY_USER.getQuery(),
                Integer.class, friendId, id));

        if (count1.isEmpty() || count2.isEmpty()) {
            throw new InternalServerException("Ошибка добавления в друзья");
        }

        if (count1.get() > 0) {
            throw new InternalServerException("Вы уже отправили заявку этому пользователю");
        } else if (count2.get() > 0) {
            log.info("Отправка запроса UPDATE_CONFIRMATION");
            int rowsUpdate = jdbc.update(Query.UPDATE_CONFIRMATION.getQuery(), friendId, id);

            if (rowsUpdate == 0) {
                throw new NotFoundException("Такого пользователя нет");
            }

            log.info("Заявка принята");
            return checkAndReturnUser(id);
        }

        log.info("Отправка запроса ADD_FRIEND");
        int rowsCreated = jdbc.update(Query.ADD_FRIEND.getQuery(), id, friendId);

        if (rowsCreated == 0) {
            throw new InternalServerException("Не удалось добавить пользователя в друзья");
        }

        return checkAndReturnUser(id);
    }

    @Override
    public User deleteFriend(int id, int friendId) {
        log.info("Отправка запроса DELETE_FRIEND");
        checkAndReturnUser(id);
        checkAndReturnUser(friendId);
        jdbc.update(Query.DELETE_FRIEND.getQuery(), id, friendId);
        return checkAndReturnUser(id);
    }

    private User checkAndReturnUser(int id) {
        return userRepository.getUser(id).orElseThrow(() -> {
            log.error("Пользователя с id {} нет", id);
            return new NotFoundException("Пользователя с id " + id + " нет");
        });
    }
}