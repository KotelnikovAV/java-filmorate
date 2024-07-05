package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Query;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.storage.BaseDbStorage.insert;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    private final JdbcTemplate jdbc;
    private final RowMapper<Review> rowMapper;

    @Override
    public Optional<Review> add(Review review) {
        log.info("Отправка запроса ADD_REVIEW");
        try {
            int id = insert(
                    jdbc,
                    Query.ADD_REVIEW.getQuery(),
                    review.getContent(),
                    review.getIsPositive(),
                    review.getUserId(),
                    review.getFilmId(),
                    review.getUseful());
            review.setReviewId(id);
            return Optional.of(review);
        } catch (DataAccessException e) {
            log.warn("Ошибка при добавлении отзыва: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Review> findById(int id) {
        log.info("Отправка запроса GET_REVIEW_WITH_FILM_ID в методе findById");
        try {
            Review review = jdbc.queryForObject(Query.GET_REVIEW_WITH_FILM_ID.getQuery(), rowMapper, id);
            return Optional.ofNullable(review);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Отзыв с id = {} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public Review update(Review review) {
        log.info("Отправка запроса UPDATE_REVIEW");
        int rowsUpdated = jdbc.update(Query.UPDATE_REVIEW.getQuery(),
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getUseful(),
                review.getReviewId());
        if (rowsUpdated == 0) {
            throw new NotFoundException("Отзыв не обновлен");
        }
        return review;
    }


    @Override
    public void deleteById(int id) {
        log.info("Отправка запроса DELETE_REVIEW");
        jdbc.update(Query.DELETE_REVIEW.getQuery(), id);
    }

    @Override
    public List<Review> findAll(int filmId, int count) {
        log.info("Получение всех отзывов по идентификатору фильма, если фильм не указан то все. " +
                "Если кол-во не указано то 10.");
        if (filmId == 0) {
            log.info("Отправка запроса GET_REVIEWS_WITH_COUNT");
            return jdbc.query(Query.GET_REVIEWS_WITH_COUNT.getQuery(), rowMapper, count);
        } else if (count == 0) {
            log.info("Отправка запроса GET_REVIEW_WITH_FILM_ID в методе findAll");
            return jdbc.query(Query.GET_REVIEW_WITH_FILM_ID.getQuery(), rowMapper, filmId);
        } else {
            log.info("Отправка запроса GET_REVIEWS");
            return jdbc.query(Query.GET_REVIEWS.getQuery(), rowMapper, filmId, count);
        }
    }

    @Override
    public void likeReview(int reviewId, int userId) {
        log.info("Отправка запроса LIKE_REVIEW");
        jdbc.update(Query.LIKE_REVIEW.getQuery(), reviewId, userId);
    }

    @Override
    public void dislikeReview(int reviewId, int userId) {
        log.info("Отправка запроса DISLIKE_REVIEW");
        jdbc.update(Query.DISLIKE_REVIEW.getQuery(), reviewId, userId);
    }

    @Override
    public void removeLikeFromReview(int reviewId, int userId) {
        log.info("Отправка запроса removeLikeFromReview");
        jdbc.update(Query.DISLIKE_REVIEW.getQuery(), reviewId, userId);
    }

    @Override
    public void removeDislikeFromReview(int reviewId, int userId) {
        log.info("Отправка запроса removeDislikeFromReview");
        jdbc.update(Query.LIKE_REVIEW.getQuery(), reviewId, userId);
    }
}
