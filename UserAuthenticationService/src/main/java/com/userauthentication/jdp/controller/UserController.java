package com.userauthentication.jdp.controller;

import com.userauthentication.jdp.entity.User;
import com.userauthentication.jdp.serviceImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * Author Name : M.V.Krishna
 * Date: 27-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Slf4j
@RestController
@RequestMapping("/userAuthService")
public class UserController {
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @Operation(summary = "Register a new user", description = "Register a new user", tags = {"user"})
     @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "success")})
    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) throws Exception {
        log.info(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("User Controller.registerUser: user: {}");
            String savedUser = this.userService.saveUser(user);


            log.info("User successfully saved with ID = {}");
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(" Exception occurred while saving user details {} ", ExceptionUtils.getStackTrace(e));
            throw new Exception(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody User users,HttpServletRequest request) throws Exception {
        log.info(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("User Controller.login: user: ");
            String token = this.userService.loginUser(users.getEmail(), users.getPassword(),request);
            log.info("User logged inn: user: ");
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            log.error(" Exception occurred while user log in ", ExceptionUtils.getStackTrace(e));
            throw new Exception(e.getMessage());
        }
    }


    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@Valid @PathVariable long userId) throws Exception {
        log.info(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            log.info("User Controller.login: user: ");
            String str = this.userService.deleteByUserId(userId);
            return new ResponseEntity<>(str, HttpStatus.OK);
        } catch (Exception e) {
            log.error(" Exception occurred while user log in ", ExceptionUtils.getStackTrace(e));
            throw new Exception(e.getMessage());
        }
    }

}
