package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.storage.ReviewRepository;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.UserEventRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository storage;
    private final UserEventRepository userEventRepository;

    @Override
    public ReviewDto add(ReviewDto review) {
        Review cratedReview = storage.add(ReviewMapper.mapToReview(review));

        log.info("Создание UserEvent добавление отзыва");
        userEventRepository.createUserEvent(UserEvent.builder()
                .userId(review.getUserId())
                .entityId(review.getReviewId())
                .eventType(EventType.REVIEW)
                .operation(Operation.ADD)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build());

        return ReviewMapper.mapToReviewDto(cratedReview);
    }

    @Override
    public ReviewDto findById(int id) {
        return ReviewMapper.mapToReviewDto(storage.findById(id));
    }

    @Override
    public ReviewDto update(ReviewDto review) {
        Review cratedReview = storage.update(ReviewMapper.mapToReview(review));

        log.info("Создание UserEvent обновление отзыва");
        userEventRepository.createUserEvent(UserEvent.builder()
                .userId(review.getUserId())
                .entityId(review.getReviewId())
                .eventType(EventType.REVIEW)
                .operation(Operation.UPDATE)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build());

        return ReviewMapper.mapToReviewDto(cratedReview);
    }

    @Override
    public void deleteById(int id) {
        Review review = storage.deleteById(id);

        log.info("Создание UserEvent удаление отзыва");
        userEventRepository.createUserEvent(UserEvent.builder()
                .userId(review.getUserId())
                .entityId(review.getReviewId())
                .eventType(EventType.REVIEW)
                .operation(Operation.REMOVE)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build());
    }

    @Override
    public List<ReviewDto> findAll(int filmId, int count) {
        List<Review> reviewDtoList = storage.findAll(filmId, count);
        return reviewDtoList.stream()
                .map(ReviewMapper::mapToReviewDto)
                .toList();
    }

    @Override
    public void likeReview(int reviewId, int userId) {
        storage.likeReview(reviewId, userId);
    }

    @Override
    public void dislikeReview(int reviewId, int userId) {
        storage.dislikeReview(reviewId, userId);
    }

    @Override
    public void removeLikeFromReview(int reviewId, int userId) {
        storage.removeLikeFromReview(reviewId, userId);
    }

    @Override
    public void removeDislikeFromReview(int reviewId, int userId) {
        storage.removeDislikeFromReview(reviewId, userId);
    }
}
