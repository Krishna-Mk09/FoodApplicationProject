package com.ApiGateway.jdp.ApiGatewayService.service;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {

        return builder.routes()
                .route("user-authentication-service", r -> r
                        .path(
                                "/userAuthService/**",
                                "/google/**"
                        )
                        .uri("lb://USER-AUTHENTICATION-SERVICE")
                )

                .route("email-service", r -> r
                        .path("/email/**")
                        .uri("lb://EMAIL-SERVICE")
                )

                .route("food-service", r -> r
                        .path("/food/**")
                        .uri("lb://FOOD-SERVICE")
                )

                .route("order-service", r -> r
                        .path("/orders/**")
                        .uri("lb://ORDER-SERVICE")
                )

                .build();
    }
}