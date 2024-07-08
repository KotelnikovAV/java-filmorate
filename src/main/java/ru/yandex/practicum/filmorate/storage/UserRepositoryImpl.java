package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
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
        log.info("Отправка запроса DELETE_MUTUAL_FRIEND");
        jdbc.update(Query.DELETE_MUTUAL_FRIEND.getQuery(), id, id);
        log.info("Отправка запроса DELETE_USER");
        jdbc.update(Query.DELETE_USER.getQuery(), id);
    }

    //add-recommendations
    @Override
    public List<FilmDto> getRecommendationsFilms(int userId) {
        log.info("Запущен метод getRecommendationsFilms для пользователя userId = {}", userId);

        List<Film> recommendationsFilms = findRecommendationsId(userId)
                .stream()
                .map(filmRepository::getFilmById)
                .toList();

        return recommendationsFilms.stream().map(FilmMapper::mapToFilmDto).toList();
    }

    @Override
    public List<Integer> findRecommendationsId(int userId) {
        log.info("Запущен вспомогательный метод findRecommendationsId, для поиска id фильмов для рекоммендаций.");

        List<Integer> userLikedFilms = likesRepository.getIdFilmsLikedByUser(userId); // получаем список ID фильмов понравившихся пользователю
        List<Integer> userIds = likesRepository.getAllUserWhoLikedFilms(); // получаем список Id ВСЕХ ПОЛЬЗОВАТЕЛЕЙ которые лайкали какие-нибудь фильмы

        if(userIds.isEmpty() && userLikedFilms.isEmpty()){  // если списки пусты возвращаем пустой список
            return new ArrayList<>();
        }

        int minLength = getSizeCommonFilmsList(userId, userIds.getFirst()); // минимальная длина списка общих фильмов, сначала берём для 1 го пользователя в списке
        int anotherUserId = userId;  // инициализируем переменную для итерации

        for (Integer id : userIds) {
            if (minLength < getSizeCommonFilmsList(userId, id)) { // если minLength меньше списка общих фильмов
                minLength = getSizeCommonFilmsList(userId, id);    // присвоим величина списка общих фильмов
                anotherUserId = id;                               // также присвоим anotherUser = id пользователя
            }
        }
        if (minLength < 0  ) {  // если minLength меньше нуля значит список пользователя больше списка друзей.
            List<Integer> filmsExceptUserLiked = filmRepository.findAll().stream().map(Film::getId).toList(); //добавим в список id всехфильмов
            filmsExceptUserLiked.retainAll(userLikedFilms); // уберём из списка все Id из списка пользователя
            return filmsExceptUserLiked; // возвращаем список id всех фильмов которые ещё не лайкал пользователь
        }

        List<Integer> anotherUserList = likesRepository.getIdFilmsLikedByUser(anotherUserId); // получим Id всех фильмов которые лайкал пользователь anotherUserId
        anotherUserList.removeAll(userLikedFilms); // уберём из списка, список с лайками userId (userLikedFilms)

        List<Integer> recommendationsId = anotherUserList;

        return recommendationsId; //вернём список
    }

    private int getSizeCommonFilmsList(int userId, int anotherId) {
        log.debug("Запущен вспомогателный метод getSizeCommonFilmsList, " +
                "для получения размера списка общих фильмов для пользователей " +
                "userId = {} и anotherId = {}", userId, anotherId);

        List<Integer> userFilm = likesRepository.getIdFilmsLikedByUser(userId);   // список ID фильмов которые понравились пользователю
        List<Integer> friendFilm = likesRepository.getIdFilmsLikedByUser(anotherId); // список ID фильмов которые понравились "другу"

        if (friendFilm.size() > userFilm.size() && userId != anotherId) {  // сравниваем размеры списков если список "друга" больше
            friendFilm.retainAll(userFilm);     // убираем из списка "друга" все общие ID
            return friendFilm.size();           // возвращаем размер этого списка
        } else if(userId == anotherId){         // если попадается сам пользователь сразу 0
            return 0;
        } else if (userFilm.size() > friendFilm.size()) { // если список пользователя больше чем список "друга"
            return friendFilm.size()- userFilm.size(); // вернём разницу между размерами  со знаком "-"
        }else {
            return 0;
        }
    }
}