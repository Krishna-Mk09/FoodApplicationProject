package com.userauthentication.jdp.controller;

import com.userauthentication.jdp.entity.User;
import com.userauthentication.jdp.service.SecurityTokenGeneratorImpl;
import com.userauthentication.jdp.service.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userService;
    private final SecurityTokenGeneratorImpl SECURITY_TOKEN_GENERATOR;


    @Autowired
    public UserController(UserServiceImpl userService, SecurityTokenGeneratorImpl securityTokenGenerator) {
        this.userService = userService;
        SECURITY_TOKEN_GENERATOR = securityTokenGenerator;
    }


    @ApiOperation(value = "Saves users in to the database  ", notes = "Saves users in to the database  ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @PostMapping("/add")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws Exception {
        // log.info(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            User savedUser = this.userService.saveUser(user);
            //log.info("User successfully saved with ID = {}", savedUser);
            return new ResponseEntity<>("saved user successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            //log.error(" Exception occurred while saving user details {} ", ExceptionUtils.getStackTrace(e));
            throw new Exception(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User users) throws Exception {
        try {
            this.userService.loginUser(users.getEmail(), users.getPassword());
            Map<String, String> secretKey = this.SECURITY_TOKEN_GENERATOR.generateToken(users);
            return new ResponseEntity<>(secretKey, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Network Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
