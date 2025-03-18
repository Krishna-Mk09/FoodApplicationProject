//package com.eureka.jdp.EurekaService.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // CSRF should be disabled for Eureka clients
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/eureka/**").authenticated() // Secure Eureka dashboard
//                        .anyRequest().permitAll()
//                )
//                .httpBasic(basic -> {}) // Use lambda syntax for HTTP Basic Auth
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
//
//        return http.build();
//    }
//}
