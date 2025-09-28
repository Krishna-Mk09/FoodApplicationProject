package com.restaurant.jdp.RestaurantService.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // Disable CSRF
                .authorizeHttpRequests(auth -> auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()  // Allow Swagger UI and API docs
                        .requestMatchers("/restaurantService/addRestaurant").permitAll()  // Allow all access to the addRestaurant endpoint
                        .anyRequest().permitAll()  // Allow all other requests without authentication
                ).sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // Stateless API

        return http.build();
    }


}
