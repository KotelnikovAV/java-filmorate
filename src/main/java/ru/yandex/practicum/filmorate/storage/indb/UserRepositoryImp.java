package ru.yandex.practicum.filmorate.storage.indb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Query;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImp implements UserRepository {

    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;

    @Override
    public Collection<User> findAll() {
        log.info("Отправка запроса FIND_ALL_USERS");
        return jdbc.query(Query.FIND_ALL_USERS.getQuery(), mapper);
    }

    @Override
    public User create(User user) {
        log.info("Отправка запроса INSERT_USER");
        int id = BaseDbStorage.insert(
                jdbc,
                Query.INSERT_USER.getQuery(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday())
        );
        user.setId(id);
        log.info("Пользователь создан с id = " + id);
        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("Отправка запроса UPDATE_USER");
        int rowsUpdated = jdbc.update(
                Query.UPDATE_USER.getQuery(),
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                Date.valueOf(newUser.getBirthday()),
                newUser.getId()
        );
        if (rowsUpdated == 0) {
            throw new NotFoundException("Такого пользователя нет");
        }
        log.info("Пользователь обновлен");
        return newUser;
    }

    @Override
    public User getUser(int id) {
        log.info("Отправка запроса FIND_USERS_BY_ID");
        return jdbc.queryForObject(Query.FIND_USERS_BY_ID.getQuery(), mapper, id);
    }

    @Override
    public List<User> findFriends(int id) {
        log.info("Отправка запроса FIND_FRIEND");
        checkUsers(id);
        List<Integer> idFriends = jdbc.queryForList(Query.FIND_FRIEND.getQuery(), Integer.class, id, id);
        return idFriends
                .stream()
                .map(this::getUser)
                .toList();
    }

    @Override
    public User addFriend(int id, int friendId) {
        checkUsers(id);
        checkUsers(friendId);
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
            return getUserWithIdFriends(id);
        }

        log.info("Отправка запроса ADD_FRIEND");
        int rowsCreated = jdbc.update(Query.ADD_FRIEND.getQuery(), id, friendId);

        if (rowsCreated == 0) {
            throw new InternalServerException("Не удалось добавить пользователя в друзья");
        }

        log.info("Заявка отправлена");
        return getUserWithIdFriends(id);
    }

    @Override
    public User deleteFriend(int id, int friendId) {
        log.info("Отправка запроса DELETE_FRIEND");
        checkUsers(id);
        checkUsers(friendId);
        jdbc.update(Query.DELETE_FRIEND.getQuery(), id, friendId);
        log.info("Пользователь удален из друзей");
        return getUserWithIdFriends(id);
    }

    private User getUserWithIdFriends(int id) {
        log.info("Начало процесса получения списка id друзей для пользователя");
        List<Integer> friends = findFriends(id)
                .stream()
                .map(User::getId)
                .toList();
        User user = getUser(id);
        user.setFriends(friends);
        return user;
    }

    private void checkUsers(int id) {
        log.info("Начало проверки на наличие пользователей в БД");
        Optional<Integer> haveUser = Optional.ofNullable(jdbc.queryForObject(Query.CHECK_USER.getQuery(),
                Integer.class, id));
        if (haveUser.isEmpty()) {
            throw new InternalServerException("Ошибка добавления в друзья");
        } else if (haveUser.get() == 0) {
            throw new NotFoundException("Такого пользователя нет");
        }
    }
}
