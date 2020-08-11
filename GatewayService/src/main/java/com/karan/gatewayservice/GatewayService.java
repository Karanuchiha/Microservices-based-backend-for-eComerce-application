package com.karan.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.security.oauth2.gateway.TokenRelayGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@Configuration
@EnableDiscoveryClient
public class GatewayService {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, TokenRelayGatewayFilterFactory filterFactory) {

        return builder.routes()
                .route(
                        r -> r.path("/users/**")
                                .uri("lb://user-service")
                )
                .route(
                        r -> r.path("/products/**")
                                .uri("lb://product-service")
                )
                .route(
                        r -> r.path("/reviews/**")
                                .uri("lb://review-service")
                )
                .route(
                        r -> r.path("/carts/**")
                                .uri("lb://sales-service")
                )
                .route(
                        r -> r.path("/orders/**")
                                .uri("lb://sales-service")
                )
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayService.class, args);
    }
}
