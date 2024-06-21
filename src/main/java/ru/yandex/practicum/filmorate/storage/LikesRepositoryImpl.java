package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Query;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LikesRepositoryImpl implements LikesRepository {

    private final JdbcTemplate jdbc;
    private final FilmRepository filmRepository;

    @Override
    public List<Film> getPopularFilms(int count) {
        log.info("Отправка запроса FIND_POPULAR_FILMS");
        List<Integer> idPopularFilms = jdbc.queryForList(Query.FIND_POPULAR_FILMS.getQuery(), Integer.class, count);
        return idPopularFilms.stream()
                .map(filmRepository::getFilmById)
                .toList();
    }

    @Override
    public Film addLike(int filmId, int userId) {
        log.info("Начало проверки наличия лайка");
        Optional<Integer> count = Optional.ofNullable(jdbc.queryForObject(Query.CHECKING_AVAILABILITY_FILM.getQuery(),
                Integer.class, filmId, userId));

        if (count.isEmpty()) {
            throw new InternalServerException("Не удалось поставить лайк");
        }

        if (count.get() > 0) {
            throw new InternalServerException("Данный пользователь уже поставил лайк");
        }

        log.info("Отправка запроса ADD_LIKE");
        int rowsCreated = jdbc.update(Query.ADD_LIKE.getQuery(), filmId, userId);

        if (rowsCreated == 0) {
            throw new InternalServerException("Не удалось поставить лайк");
        }

        log.info("Лайк поставлен");
        return filmRepository.getFilmById(filmId);
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        log.info("Отправка запроса DELETE_LIKE");
        int rowsDeleted = jdbc.update(Query.DELETE_LIKE.getQuery(), userId);

        if (rowsDeleted == 0) {
            throw new InternalServerException("Данный пользователь лайк не ставил");
        }

        log.info("Лайк удален");
        return filmRepository.getFilmById(filmId);
    }

    @Override
    public List<Integer> getListLikes(Film film) {
        log.info("Отправка запроса FIND_LIST_LIKES");
        return jdbc.queryForList(Query.FIND_LIST_LIKES.getQuery(), Integer.class, film.getId());
    }
}
