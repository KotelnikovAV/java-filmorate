package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewRepository {

    List<Review> findAll(int filmId, int count);

    Review findById(int id);

    Review add(Review review);

    Review update(Review review);

    void deleteById(int id);

    void likeReview(int reviewId, int userId);

    void dislikeReview(int reviewId, int userId);

    void removeLikeFromReview(int reviewId, int userId);

    void removeDislikeFromReview(int reviewId, int userId);
}
