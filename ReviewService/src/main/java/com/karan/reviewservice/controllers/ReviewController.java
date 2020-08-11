package com.karan.reviewservice.controllers;

import com.karan.reviewservice.exceptions.ReviewException;
import com.karan.reviewservice.model.Review;
import com.karan.reviewservice.model.service.ReviewRequestService;
import com.karan.reviewservice.repositories.ReviewRepository;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private Environment env;
    @Autowired
    ReviewRequestService reviewRequestService;
    @Autowired
    WebClient client;

    @CrossOrigin
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Review>> getReview(@PathVariable("id") String id) {
        return reviewRepository.findById(id).map(review -> ResponseEntity.status(HttpStatus.OK).body(review)).defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @CrossOrigin
    @PostMapping()
    @PreAuthorize("hasAuthority('Everyone')")
    public Mono<ResponseEntity<Review>> createReview(@Valid @RequestBody Review review, @RequestHeader(name = "Authorization") String token) throws ReviewException {
        Optional<Review> existingReview = reviewRepository.findByUserIdAndProductId(review.getUserId(), review.getProductId()).blockOptional();
        if (existingReview.isPresent()) {
            throw new ReviewException(String.format("User with id: %s already has a review for this product", review.getUserId()));
        } else {
            if (createValidReview(review, token)) {
                Review reviewToSave = new Review(review);
                return reviewRepository.save(reviewToSave).map(review1 -> ResponseEntity.status(HttpStatus.OK).body(review1));

            }
        }
        return Mono.just(ResponseEntity.badRequest().body(review));
    }


    private boolean createValidReview(Review review, String token) throws ReviewException {
        return reviewRequestService.requestService(review, env.getProperty("gateway.address"), client, token);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Admin')")
    public Mono<ResponseEntity<Void>> deleteReviewByUserId(@PathVariable("id") String id) {
        return reviewRepository.findById(id).flatMap(review -> reviewRepository.delete(review).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @CrossOrigin
    @GetMapping("/products/{productId}")
    public Mono<ResponseEntity<List<Review>>> searchByProductId(
            @PathVariable("productId") String productId) {
        Mono<List<Review>> reviews=  reviewRepository.findByProductId(productId).collectList();
        return reviews.map(reviews1 -> ResponseEntity.status(HttpStatus.OK).body(reviews1)).defaultIfEmpty(ResponseEntity.noContent().build());

    }

    @CrossOrigin
    @GetMapping("/users/{userId}")
    public Mono<ResponseEntity<List<Review>>> searchByUserId(@PathVariable("userId") String userId) {
        Mono<List<Review>> reviews=  reviewRepository.findByUserId(userId).collectList();
        return reviews.map(reviews1 -> ResponseEntity.status(HttpStatus.OK).body(reviews1)).defaultIfEmpty(ResponseEntity.noContent().build());
    }
}