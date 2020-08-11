package com.karan.reviewservice.model.service;

import com.karan.reviewservice.model.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class ReviewRequestService {


    public boolean requestService(Review review, String gateway, WebClient webClient, String token)  {
        boolean isValidReview = false;
        ResponseEntity validUserResponse = getEntity(gateway, "/users/", review.getUserId(),
                webClient, token).block();
//        ResponseEntity validProductResponse = getEntity(gateway, "/products/",
//                review.getProductId(), webClient, token).block();
        log.info("check");
        if (validUserResponse.getStatusCode().is2xxSuccessful()
                 /*validProductResponse.getStatusCode().is2xxSuccessful()*/) {
            log.info("oh yess");
            isValidReview = true;
        }
        return isValidReview;
    }


    public Mono<ResponseEntity> getEntity(String gateway, String microservice, String id,
                                                  WebClient webClient, String token)  {
        Mono<ResponseEntity> response =webClient.get().uri(microservice + id).header("Authorization", token).retrieve().bodyToMono(ResponseEntity.class);

        if(response.block().getStatusCode().is2xxSuccessful()){
            log.info("I win");
        }
        Mono<ResponseEntity> s = HystrixCommands
                .from(response)
                .fallback( Emergency()).commandName("getEntities").toMono();

        return s;
    }

    public Mono<ResponseEntity> Emergency() {
        log.info("very bad");
        return Mono.just(ResponseEntity.badRequest().build());
    }

    public Mono<ResponseEntity> getEntities(String gateway, String microservice, String id,
                                          WebClient webClient, String token){
        log.info("very good");
        return   webClient.get().uri(microservice + id).header("Authorization", token).retrieve().bodyToMono(ResponseEntity.class);


    }
}
