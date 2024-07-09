package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.storage.ReviewRepository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository storage;

    @Override
    public ReviewDto add(ReviewDto review) {
        log.info("Начало процесса получения всех отзывов");
        Review cratedReview = storage.add(ReviewMapper.mapToReview(review));
        log.info("Список всех отзывов получен");
        return ReviewMapper.mapToReviewDto(cratedReview);
    }

    @Override
    public ReviewDto findById(int id) {
        log.info("Начало процесса получения отзыва по id = {}", id);
        log.info("Отзыв получен");
        return ReviewMapper.mapToReviewDto(storage.findById(id));
    }

    @Override
    public ReviewDto update(ReviewDto review) {
        log.info("Начало процесса обновления отзыва");
        Review cratedReview = storage.update(ReviewMapper.mapToReview(review));
        log.info("Отзыв обновлен");
        return ReviewMapper.mapToReviewDto(cratedReview);
    }

    @Override
    public void deleteById(int id) {
        log.info("Начало процесса удаления отзыва");
        storage.deleteById(id);
        log.info("Отзыв удален");
    }

    @Override
    public List<ReviewDto> findAll(int filmId, int count) {
        log.info("Начало процесса получения всех отзывов");
        List<Review> reviewDtoList = storage.findAll(filmId, count);
        log.info("Список всех отзывов получен");
        return reviewDtoList.stream()
                .map(ReviewMapper::mapToReviewDto)
                .toList();
    }

    @Override
    public void likeReview(int reviewId, int userId) {
        log.info("Начало процесса: пользователь ставит лайк отзыву.");
        log.debug("Значения переменных при добавлении лайка отзыву reviewId и userId: {}, {}", reviewId, userId);
        storage.likeReview(reviewId, userId);
    }

    @Override
    public void dislikeReview(int reviewId, int userId) {
        log.info("Начало процесса: пользователь ставит дизлайк отзыву.");
        log.debug("Значения переменных при добавлении лайка отзыву reviewId и userId: {}, {}", reviewId, userId);
        storage.dislikeReview(reviewId, userId);
    }

    @Override
    public void removeLikeFromReview(int reviewId, int userId) {
        log.info("Начало процесса: пользователь удаляет лайк/дизлайк отзыву..");
        log.debug("Значения переменных при добавлении лайка отзыву reviewId и userId: {}, {}", reviewId, userId);
        storage.removeLikeFromReview(reviewId, userId);
    }

    @Override
    public void removeDislikeFromReview(int reviewId, int userId) {
        log.info("Начало процесса: пользователь удаляет дизлайк отзыву.");
        log.debug("Значения переменных при добавлении лайка отзыву reviewId и userId: {}, {}", reviewId, userId);
        storage.removeDislikeFromReview(reviewId, userId);
    }
}
