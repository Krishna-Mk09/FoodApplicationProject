package com.userauthentication.jdp.config;

import com.userauthentication.jdp.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

/*
 * Author Name : M.V.Krishna
 * Date: 28-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(secretKey);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(org.springframework.security.config.Customizer.withDefaults()).csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth.requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll().requestMatchers("/error").permitAll().requestMatchers("/userAuthService/registerUser", "/userAuthService/login", "/userAuthService/send-otp/**", "/google/**", "/userAuthService/verify-otp/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll().requestMatchers("/admin/**").hasRole("ADMIN").requestMatchers("/userAuthService/deleteUser/**").hasRole("ADMIN").requestMatchers("/userAuthService/**").hasAnyRole("USER", "OWNER").anyRequest().authenticated()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}




