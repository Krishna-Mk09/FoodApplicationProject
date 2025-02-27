package com.userauthentication.jdp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * Author Name : M.V.Krishna
 * Date: 28-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Configuration

public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Use strength 12 for better security
    }
}
