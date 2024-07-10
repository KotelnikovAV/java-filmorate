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
import java.util.ArrayList;
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
        log.info("Отправка запроса DELETE_USER");
        jdbc.update(Query.DELETE_USER.getQuery(), id);
    }

    @Override
    public List<Film> findRecommendationsId(int userId) {
        log.info("Запущен вспомогательный метод findRecommendationsId, для поиска id фильмов для рекоммендаций.");
        List<Integer> userLikedFilms = likesRepository.getIdFilmsLikedByUser(userId);
        List<Integer> userIds = getAllUserWhoLikedFilms();

        if (userIds.isEmpty() || userLikedFilms.isEmpty()) {
            return new ArrayList<>();
        } else if (userIds.size() == 1) {
            return new ArrayList<>();
        }

        int countSameLikes = -1;
        int anotherUserId = userId;

        for (Integer id : userIds) {
            if (countSameLikes < getSizeCommonFilmsList(userId, id)) {
                countSameLikes = getSizeCommonFilmsList(userId, id);
                anotherUserId = id;
            }
        }

        if (countSameLikes < 0) {
            List<Integer> filmsExceptUserLiked = new ArrayList<>(filmRepository.findAll().stream()
                    .map(Film::getId)
                    .toList());
            filmsExceptUserLiked.retainAll(userLikedFilms);
            return filmsExceptUserLiked.stream()
                    .map(filmRepository::getFilmById)
                    .toList();
        }

        List<Integer> recommendationsId = likesRepository.getIdFilmsLikedByUser(anotherUserId);
        recommendationsId.removeAll(userLikedFilms);

        return recommendationsId
                .stream()
                .map(filmRepository::getFilmById)
                .toList();
    }

    private int getSizeCommonFilmsList(int userId, int anotherId) {
        log.debug("Запущен вспомогателный метод getSizeCommonFilmsList, " +
                "для получения размера списка общих фильмов для пользователей " +
                "userId = {} и anotherId = {}", userId, anotherId);

        List<Integer> userFilm = likesRepository.getIdFilmsLikedByUser(userId);
        List<Integer> friendFilm = likesRepository.getIdFilmsLikedByUser(anotherId);

        if (friendFilm.size() > userFilm.size() && userId != anotherId) {
            friendFilm.retainAll(userFilm);
            return friendFilm.size();
        } else if (userFilm.size() > friendFilm.size()) {
            userFilm.retainAll(friendFilm);
            return -1 * userFilm.size();
        } else {
            return 0;
        }
    }

    private List<Integer> getAllUserWhoLikedFilms() {
        log.info("Отправка запроса GET_USERS_ID_FROM_FILMS_LIKE");
        return jdbc.queryForList(Query.GET_USERS_ID_FROM_FILMS_LIKE.getQuery(), Integer.class);
    }
}