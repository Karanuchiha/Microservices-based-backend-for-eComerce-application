package com.karan.reviewservice.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.karan.reviewservice.exceptions.ReviewException;
import com.karan.reviewservice.model.Review;
import com.karan.reviewservice.model.service.ReviewRequestService;
import com.karan.reviewservice.repositories.ReviewRepository;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
public class ReviewControllerTest {

  Review review = new Review("6", "3", "125", "very efficient", 5);
  Review reviewTosave = new Review("2", "2", "125", "very efficient", 5);
  private String correctId = "correctId";
  private String wrongId = "wrongId";
  private String token="any token";

  @Mock
  ReviewRepository mockedReviewRepository;
  @Mock
  ReviewRequestService mockedReviewRequestService;
  @Mock
  Environment mockedEnv;
  @InjectMocks
  ReviewController reviewController;


  @Test
  public void positiveGetReview() {
    when(mockedReviewRepository.findById(anyString())).thenReturn(Mono.just(review));
    assertEquals(ResponseEntity.ok(review), reviewController.getReview(correctId).block());
  }

  @Test
  public void negativeGetReview() {
    when(mockedReviewRepository.findById(wrongId)).thenReturn(Mono.empty());
    assertEquals(ResponseEntity.badRequest().build(), reviewController.getReview(wrongId).block());
  }

  @Test
  void positiveCreateReviewTest() throws ReviewException {
    when(mockedReviewRequestService.requestService(any(), anyString(), any(),anyString())).thenReturn(true);
    when(mockedEnv.getProperty(anyString())).thenReturn("Test");
    when(mockedReviewRepository.findByUserIdAndProductId(anyString(),anyString())).thenReturn(Mono.empty());
    when(mockedReviewRepository.save(any(Review.class))).thenReturn(Mono.just(reviewTosave));
    ResponseEntity<Review> response = reviewController.createReview(reviewTosave,token).block();

    assertEquals(response.getStatusCode(), ResponseEntity.ok().build().getStatusCode());
  }

  @Test
  void negativeCreateReviewTest() throws ReviewException {
    when(mockedReviewRequestService.requestService(any(), anyString(), any(),anyString())).thenReturn(false);
    when(mockedEnv.getProperty(anyString())).thenReturn("Test");
    when(mockedReviewRepository.findByUserIdAndProductId(anyString(),anyString())).thenReturn(Mono.empty());
    when(mockedReviewRepository.save(any(Review.class))).thenReturn(Mono.just(reviewTosave));
    ResponseEntity<Review> response = reviewController.createReview(reviewTosave,token).block();

    //assertEquals(response.getStatusCode(), ResponseEntity.ok().build().getStatusCode());
    assertEquals(response.getStatusCode(), ResponseEntity.badRequest().build().getStatusCode());
  }

  @Test
  void existingReviewCreateReviewTest() {
    HttpStatus expectedResponse = HttpStatus.BAD_REQUEST;
    when(mockedReviewRepository.findByUserIdAndProductId(anyString(), anyString()))
        .thenReturn(Mono.just(review));
    ResponseStatusException response = assertThrows(ResponseStatusException.class,
        () -> reviewController.createReview(reviewTosave,token));
    assertEquals(expectedResponse, response.getStatus());
  }

  @Test
  public void positiveDeleteReviewTest() {
    when(mockedReviewRepository.findById(correctId)).thenReturn(Mono.just(review));
    when(mockedReviewRepository.delete(review)).thenReturn(Mono.empty());
    assertEquals(ResponseEntity.ok().build(), reviewController.deleteReviewByUserId(correctId).block());
  }

  @Test
  public void negativeDeleteReviewTest() {
    when(mockedReviewRepository.findById(wrongId)).thenReturn(Mono.empty());
    when(mockedReviewRepository.delete(review)).thenReturn(Mono.empty());
    assertEquals(ResponseEntity.badRequest().build(),
        reviewController.deleteReviewByUserId(wrongId).block());
  }

  @Test
  public void positiveSearchByProductId() {
    List<Review> reviews = new ArrayList<>();
    reviews.add(review);
    when(mockedReviewRepository.findByProductId(anyString())).thenReturn(Flux.just(review));
    assertEquals(ResponseEntity.ok(reviews), reviewController.searchByProductId("").block());
  }

  @Test
  public void negativeSearchByProductId() {
    List<Review> reviews = new ArrayList<>();
    when(mockedReviewRepository.findByProductId(anyString())).thenReturn(Flux.empty());
    assertEquals(ResponseEntity.ok(reviews), reviewController.searchByProductId("").block());
  }

  @Test
  public void positiveSearchByUserId() {
    List<Review> reviews = new ArrayList<>();
    reviews.add(review);

    when(mockedReviewRepository.findByUserId(anyString())).thenReturn(Flux.just(review));
    assertEquals(ResponseEntity.ok(reviews), reviewController.searchByUserId("").block());
  }

  @Test
  public void negativeSearchByUserId() {
    List<Review> reviews = new ArrayList<>();
    when(mockedReviewRepository.findByUserId(anyString())).thenReturn(Flux.empty());
    assertEquals(ResponseEntity.ok(reviews), reviewController.searchByUserId("").block());
  }
}
