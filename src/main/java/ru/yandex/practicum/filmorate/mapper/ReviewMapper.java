package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.model.Review;

@UtilityClass
@Slf4j
public class ReviewMapper {

    public Review mapToReview(ReviewDto reviewDto) {
        log.info("Начало преобразования ReviewDto в Review");

        if (reviewDto == null) {
            return null;
        }
        Review review = Review.builder()
                .reviewId(reviewDto.getReviewId())
                .content(reviewDto.getContent())
                .isPositive(reviewDto.getIsPositive())
                .userId(reviewDto.getUserId())
                .filmId(reviewDto.getFilmId())
                .useful(reviewDto.getUseful())
                .build();

        log.info("Преобразование ReviewDto в Review успешно завершено");
        return review;

    }

    public ReviewDto mapToReviewDto(Review review) {
        log.info("Начало преобразования Review в ReviewDto");

        if (review == null) {
            return null;
        }

        ReviewDto reviewDto = ReviewDto.builder()
                .reviewId(review.getReviewId())
                .content(review.getContent())
                .isPositive(review.getIsPositive())
                .userId(review.getUserId())
                .filmId(review.getFilmId())
                .useful(review.getUseful())
                .build();

        log.info("Преобразование Review в ReviewDto успешно завершено");
        return reviewDto;
    }
}