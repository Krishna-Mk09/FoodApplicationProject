package com.userauthentication.jdp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        http.csrf(csrf -> csrf.disable()) // Disabling CSRF for API access
                .authorizeHttpRequests(auth -> auth.requestMatchers("/userAuthService/registerUser", "/userAuthService/login", "/userAuthService/send-otp/**", "/api/email/send-email", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll().anyRequest().authenticated()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


}


