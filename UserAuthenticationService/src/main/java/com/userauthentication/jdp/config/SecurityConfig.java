package com.userauthentication.jdp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Author Name : M.V.Krishna
 * Date: 28-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disabling CSRF for testing
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/userAuthService/registerUser","/userAuthService/login").permitAll() // Corrected endpoint
                                .requestMatchers(HttpMethod.GET, "/userAuthService/**").permitAll() // Allow public GET requests
                                .requestMatchers(HttpMethod.POST, "/userAuthService/**").authenticated() // Require authentication for CREATE
                                .requestMatchers(HttpMethod.PUT, "/userAuthService/**").authenticated() // Require authentication for UPDATE
                                .requestMatchers(HttpMethod.DELETE, "/userAuthService/**").authenticated()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}


