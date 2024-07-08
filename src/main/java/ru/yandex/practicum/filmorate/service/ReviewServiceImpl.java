package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.ReviewRepository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository storage;

    @Override
    public Review add(Review review) {
        return storage.add(review);
    }

    @Override
    public Review findById(int id) {
        return storage.findById(id);
    }

    @Override
    public Review update(Review review) {
        return storage.update(review);
    }

    @Override
    public void deleteById(int id) {
        storage.deleteById(id);
    }

    @Override
    public List<Review> findAll(int filmId, int count) {
        return storage.findAll(filmId, count);
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
