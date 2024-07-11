package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.ReviewDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReviewDto add(@Valid @RequestBody ReviewDto review) {
        log.info("Получен HTTP-запрос по адресу /reviews (метод POST). Вызван метод add(review)");
        return reviewService.add(review);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public ReviewDto update(@Valid @RequestBody ReviewDto review) {
        log.info("Получен HTTP-запрос по адресу /reviews (метод PUT). Вызван метод update(review)");
        return reviewService.update(review);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /reviews/{id} (метод DELETE). Вызван метод deleteById(id)");
        reviewService.deleteById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public ReviewDto findById(@PathVariable int id) {
        log.info("Получен HTTP-запрос по адресу /reviews/{id} (метод GET). Вызван метод findById(id)");
        return reviewService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReviewDto> findAll(@RequestParam(defaultValue = "0") int filmId,
                                   @RequestParam(defaultValue = "10") int count) {
        log.info("Получен HTTP-запрос по адресу /reviews?filmId={filmId}&count={count} (метод GET). " +
                "Вызван метод findAll(filmId, count)");
        return reviewService.findAll(filmId, count);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}/like/{userId}")
    public void likeReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("Получен HTTP-запрос по адресу /reviews/{id}/like/{userId} (метод PUT). " +
                "Вызван метод likeReview(id, userId)");
        reviewService.likeReview(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("Получен HTTP-запрос по адресу /reviews/{id}/dislike/{userId} (метод PUT). " +
                "Вызван метод dislikeReview(id, userId)");
        reviewService.dislikeReview(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}/like/{userId}")
    public void removeLikeFromReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("Получен HTTP-запрос по адресу /reviews/{id}/like/{userId} (метод DELETE). " +
                "Вызван метод removeLikeFromReview(id, userId)");
        reviewService.removeLikeFromReview(id, userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}/dislike/{userId}")
    public void removeDislikeFromReview(@PathVariable("id") int id, @PathVariable("userId") int userId) {
        log.info("Получен HTTP-запрос по адресу /reviews/{id}/dislike/{userId} (метод DELETE). " +
                "Вызван метод removeDislikeFromReview(id, userId)");
        reviewService.removeDislikeFromReview(id, userId);
    }
}
