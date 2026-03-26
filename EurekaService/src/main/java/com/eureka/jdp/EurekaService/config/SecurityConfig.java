package com.eureka.jdp.EurekaService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/eureka/**").authenticated() // Secure Eureka endpoints
                        .anyRequest().permitAll()).httpBasic(Customizer.withDefaults())  // Updated way of enabling HTTP Basic Auth
                .csrf(csrf -> csrf.ignoringRequestMatchers("/eureka/**")); // Ignore CSRF for Eureka clients

        return http.build();
    }
}
