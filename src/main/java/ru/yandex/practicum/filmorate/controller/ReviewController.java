package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ReviewDto addReview(@Valid @RequestBody ReviewDto reviewDto) {
        return reviewService.add(reviewDto);
    }

    @PutMapping
    public ReviewDto updateReview(@Valid @RequestBody ReviewDto review) {
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id) {
        reviewService.deleteById(id);
    }

    @GetMapping("/{id}")
    public ReviewDto getReviewById(@PathVariable int id) {
        return reviewService.findById(id);
    }

    @GetMapping
    public List<ReviewDto> getReviews(@RequestParam(required = false) int filmId,
                                      @RequestParam(defaultValue = "10", required = false) int count) {
        return reviewService.findAll(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.likeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.dislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFromReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.removeLikeFromReview(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void removeDislikeFromReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        reviewService.removeDislikeFromReview(id, userId);
    }
}