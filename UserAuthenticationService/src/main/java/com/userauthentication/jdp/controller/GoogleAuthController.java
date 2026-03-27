package com.userauthentication.jdp.controller;

import com.userauthentication.jdp.beans.EmailRequest;
import com.userauthentication.jdp.beans.GoogleUserInfo;
import com.userauthentication.jdp.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/*
 * Author Name : P.Ramcharan Teja
 * Date: 27-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/google")
public class GoogleAuthController {

    private static final String TOPIC = "email";
    private final UserServiceImpl userService;
    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;

    @GetMapping("/login")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String url = userService.buildGoogleAuthUrl();
        log.info("Redirecting to Google Auth URL: {}", url);
        response.sendRedirect(url);
    }


    @GetMapping("/callback")
    public void googleCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.info("Google callback hit with code: {}", code);
        Map<String, String> tokenResponse = userService.exchangeCodeForToken(code);
        GoogleUserInfo profile = userService.fetchUserProfile(tokenResponse.get("access_token"));
        String jwtToken = userService.handleGoogleLogin(profile);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setSenderEmail(profile.getEmail());
        emailRequest.setSubject("Registration Acknowledgement");
        emailRequest.setTemplateName("welcome-email");
        emailRequest.setUserName(profile.getName());
        kafkaTemplate.send(TOPIC, profile.getEmail(), emailRequest);
        response.sendRedirect("http://localhost:4200/login?token=" + jwtToken);
    }
}
