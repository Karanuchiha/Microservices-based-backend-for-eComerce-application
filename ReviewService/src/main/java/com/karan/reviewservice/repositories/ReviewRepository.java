package com.karan.reviewservice.repositories;

import com.karan.reviewservice.model.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {

  Flux<Review> findByProductId(String productId);

  Flux<Review> findByUserId(String userId);

  Mono<Review> findByUserIdAndProductId(String userId, String productId);
}
