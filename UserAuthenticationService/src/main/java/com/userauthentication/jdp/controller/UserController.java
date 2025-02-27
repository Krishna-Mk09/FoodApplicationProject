package com.userauthentication.jdp.controller;

import com.userauthentication.jdp.entity.User;
import com.userauthentication.jdp.service.SecurityTokenGeneratorImpl;
import com.userauthentication.jdp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/*
 * Author Name : M.V.Krishna
 * Date: 27-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;
    private final SecurityTokenGeneratorImpl SECURITY_TOKEN_GENERATOR;


    @Autowired
    public UserController(UserServiceImpl userService, SecurityTokenGeneratorImpl securityTokenGenerator) {
        this.userService = userService;
        SECURITY_TOKEN_GENERATOR = securityTokenGenerator;
    }


    @PostMapping("/add")
    public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception {
        try {
            return new ResponseEntity<>(this.userService.saveUser(user), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) throws Exception {
        try {
            this.userService.loginUser(user.getEmail(), user.getPassword());
            Map<String, String> secretKey = this.SECURITY_TOKEN_GENERATOR.generateToken(user);
            return new ResponseEntity<>(secretKey, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Network Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
