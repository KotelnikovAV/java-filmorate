package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Query;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbc;
    private final RowMapper<User> mapper;
    private final FilmRepository filmRepository;
    private final LikesRepository likesRepository;

    @Override
    public List<User> findAll() {
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

        return newUser;
    }

    @Override
    public User getUser(int id) {
        log.info("Отправка запроса FIND_USERS_BY_ID");
        return jdbc.queryForObject(Query.FIND_USERS_BY_ID.getQuery(), mapper, id);
    }

    @Override
    public void delete(int id) {
        log.info("Отправка запроса DELETE_MUTUAL_FRIEND");
        jdbc.update(Query.DELETE_MUTUAL_FRIEND.getQuery(), id, id);
        log.info("Отправка запроса DELETE_USER");
        jdbc.update(Query.DELETE_USER.getQuery(), id);
    }

    //add-recommendations
    @Override
    public List<Film> getRecommendationsFilms(int userId) {
        log.info("Запущен метод getRecommendationsFilms для пользователя userId = {}", userId);

        List<Film> recommendationsFilms = findRecommendationsId(userId)
                .stream()
                .map(filmRepository::getFilmById)
                .toList();

        return recommendationsFilms;
    }

    @Override
    public List<Integer> findRecommendationsId(int userId) {
        log.info("Запущен вспомогательный метод findRecommendationsId, для поиска id фильмов для рекоммендаций.");

        List<Integer> userLikedFilms = likesRepository.getIdFilmsLikedByUser(userId);
        List<Integer> userIds = likesRepository.getAllUserWhoLikedFilms();

        int minLength = getSizeCommonFilmsList(userId, userIds.getFirst());
        int anotherUserId = userId;

        for (Integer id : userIds) {
            if (minLength < getSizeCommonFilmsList(userId, id)) {
                minLength = getSizeCommonFilmsList(userId, id);
                anotherUserId = id;
            }
        }

        List<Integer> anotherUserList = likesRepository.getIdFilmsLikedByUser(anotherUserId);
        anotherUserList.removeAll(userLikedFilms);

        List<Integer> recommendationsId = anotherUserList;

        return recommendationsId;
    }

    private int getSizeCommonFilmsList(int userId, int anotherId) {
        log.debug("Запущен вспомогателный метод getSizeCommonFilmsList, " +
                "для получения размера списка общих фильмов для пользователей " +
                "userId = {} и anotherId = {}", userId, anotherId);

        List<Integer> userFilm = likesRepository.getIdFilmsLikedByUser(userId);
        List<Integer> friendFilm = likesRepository.getIdFilmsLikedByUser(anotherId);

        if (friendFilm.size() > userFilm.size()) {
            friendFilm.retainAll(userFilm);
            return friendFilm.size();
        } else {
            return 0;
        }
    }
}