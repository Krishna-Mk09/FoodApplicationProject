package com.userauthentication.jdp.controller;

import com.userauthentication.jdp.serviceImpl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/*
 * Author Name : P.Ramcharan Teja
 * Date: 27-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

@Slf4j
@RestController
@RequestMapping("/google")
public class GoogleAuthController {

    private final UserServiceImpl userService;

    @Autowired
    public GoogleAuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String url = userService.buildGoogleAuthUrl();
        log.info("Redirecting to Google Auth URL: {}", url);
        response.sendRedirect(url);
    }

    /**
     * Step 2: Callback after Google login
     */
    @GetMapping("/callback")
    public ResponseEntity<?> googleCallback(@RequestParam("code") String code) {
        log.info("Google callback hit with code: {}", code);
         String jwtToken = userService.handleGoogleLogin(code);
         return ResponseEntity.ok(Map.of("jwtToken", jwtToken));
    }
}
