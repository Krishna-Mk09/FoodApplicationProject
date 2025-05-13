package com.restaurant.jdp.RestaurantService.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
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
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())  // Disable CSRF for stateless APIs
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()  // Permit all for swagger-related paths
//                        .requestMatchers("/restaurantService/addRestaurant").permitAll()  // Permit all for addRestaurant endpoint
//                        .anyRequest().authenticated()  // Require authentication for other requests
//                )
//                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless API
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))  // Enable JWT authentication
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint(new org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint())  // Handle 401 Unauthorized
//                        .accessDeniedHandler(new org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler())  // Handle 403 Forbidden
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        SecretKey key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
//        return NimbusJwtDecoder.withSecretKey(key).build();
//    }
//
//    private JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        authoritiesConverter.setAuthorityPrefix("ROLE_");
//        authoritiesConverter.setAuthoritiesClaimName("role");  // Ensure that "role" claim in JWT matches the one in token
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
//        return converter;
//    }
}
