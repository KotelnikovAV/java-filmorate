package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    List<ReviewDto> findAll(int filmId, int count);

    ReviewDto findById(int id);

    ReviewDto add(ReviewDto review);

    ReviewDto update(ReviewDto review);

    void deleteById(int id);

    void likeReview(int reviewId, int userId);

    void dislikeReview(int reviewId, int userId);

    void removeLikeFromReview(int reviewId, int userId);

    void removeDislikeFromReview(int reviewId, int userId);
}
