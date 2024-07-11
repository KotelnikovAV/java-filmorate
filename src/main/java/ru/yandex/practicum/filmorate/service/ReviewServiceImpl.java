package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.ReviewRepository;
import ru.yandex.practicum.filmorate.storage.UserEventRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserEventRepository userEventRepository;

    @Override
    public ReviewDto add(ReviewDto review) {
        if (review.getFilmId() < 0 || review.getUserId() < 0) {
            throw new NotFoundException("Такого фильма или пользователя не существует");
        }
        log.info("Начало процесса добавления отзыва");
        Review cratedReview = reviewRepository.add(ReviewMapper.mapToReview(review));
        log.info("Отзыв успешно добавлен");

        log.info("Создание UserEvent добавление отзыва");
        userEventRepository.createUserEvent(UserEvent.builder()
                .userId(review.getUserId())
                .entityId(cratedReview.getReviewId())
                .eventType(EventType.REVIEW)
                .operation(Operation.ADD)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build());
        log.info("Создание UserEvent добавление отзыва успешно завершено");

        return ReviewMapper.mapToReviewDto(cratedReview);
    }

    @Override
    public ReviewDto findById(int id) {
        log.info("Начало процесса получения отзыва по id = {}", id);
        Review review = reviewRepository.findById(id);
        log.info("Отзыв получен");
        return ReviewMapper.mapToReviewDto(review);
    }

    @Override
    public ReviewDto update(ReviewDto review) {
        log.info("Начало процесса обновления отзыва");
        Review cratedReview = reviewRepository.update(ReviewMapper.mapToReview(review));

        log.info("Создание UserEvent обновление отзыва");
        userEventRepository.createUserEvent(UserEvent.builder()
                .userId(cratedReview.getUserId())
                .entityId(cratedReview.getReviewId())
                .eventType(EventType.REVIEW)
                .operation(Operation.UPDATE)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build());
        log.info("Создание UserEvent обновление отзыва успешно завершено");

        log.info("Отзыв обновлен");
        return ReviewMapper.mapToReviewDto(cratedReview);
    }

    @Override
    public void deleteById(int id) {
        Review review = reviewRepository.findById(id);
        log.info("Начало процесса удаления отзыва");
        reviewRepository.deleteById(id);
        log.info("Отзыв удален");

        log.info("Создание UserEvent удаление отзыва");
        userEventRepository.createUserEvent(UserEvent.builder()
                .userId(review.getUserId())
                .entityId(review.getReviewId())
                .eventType(EventType.REVIEW)
                .operation(Operation.REMOVE)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build());
        log.info("Создание UserEvent удаление отзыва успешно завершено");
    }

    @Override
    public List<ReviewDto> findAll(int filmId, int count) {
        log.info("Начало процесса получения всех отзывов");
        List<Review> reviewDtoList = reviewRepository.findAll(filmId, count);
        log.info("Список всех отзывов получен");
        return reviewDtoList.stream()
                .map(ReviewMapper::mapToReviewDto)
                .toList();
    }

    @Override
    public void likeReview(int reviewId, int userId) {
        log.info("Начало процесса: пользователь ставит лайк отзыву.");
        log.debug("Значения переменных при добавлении лайка отзыву reviewId и userId: {}, {}", reviewId, userId);
        reviewRepository.likeReview(reviewId, userId);
        log.info("Лайк ревью успешно поставлен");
    }

    @Override
    public void dislikeReview(int reviewId, int userId) {
        log.info("Начало процесса: пользователь ставит дизлайк отзыву.");
        log.debug("Значения переменных при добавлении дизлайка отзыву reviewId и userId: {}, {}", reviewId, userId);
        reviewRepository.dislikeReview(reviewId, userId);
        log.info("Дизлайк ревью успешно поставлен");
    }

    @Override
    public void removeLikeFromReview(int reviewId, int userId) {
        log.info("Начало процесса: пользователь удаляет лайк/дизлайк отзыву..");
        log.debug("Значения переменных при удалении лайка отзыву reviewId и userId: {}, {}", reviewId, userId);
        reviewRepository.removeLikeFromReview(reviewId, userId);
        log.info("Лайк ревью успешно удален");
    }

    @Override
    public void removeDislikeFromReview(int reviewId, int userId) {
        log.info("Начало процесса: пользователь удаляет дизлайк отзыву.");
        log.debug("Значения переменных при удалении дизлайка отзыву reviewId и userId: {}, {}", reviewId, userId);
        reviewRepository.removeDislikeFromReview(reviewId, userId);
        log.info("Дизлайк ревью успешно удален");
    }
}