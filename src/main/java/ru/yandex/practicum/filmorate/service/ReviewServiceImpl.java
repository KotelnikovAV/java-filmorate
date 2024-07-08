package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.storage.ReviewRepository;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository storage;

    @Override
    public ReviewDto add(ReviewDto review) {
        Review cratedReview = storage.add(ReviewMapper.mapToReview(review));
        return ReviewMapper.mapToReviewDto(cratedReview);
    }

    @Override
    public ReviewDto findById(int id) {
        return ReviewMapper.mapToReviewDto(storage.findById(id));
    }

    @Override
    public ReviewDto update(ReviewDto review) {
        Review cratedReview = storage.update(ReviewMapper.mapToReview(review));
        return ReviewMapper.mapToReviewDto(cratedReview);
    }

    @Override
    public void deleteById(int id) {
        storage.deleteById(id);
    }

    @Override
    public List<ReviewDto> findAll(int filmId, int count) {
        //return storage.findAll(filmId, count);
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
