package com.userauthentication.jdp.controller;

import com.foodapplication.jdp.Common_Service.Entity.UserDTO;
import com.userauthentication.jdp.beans.UserUpdate;
import com.userauthentication.jdp.beans.UsersDTO;
import com.userauthentication.jdp.entity.User;
import com.userauthentication.jdp.serviceImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
@RequiredArgsConstructor
@RequestMapping("/userAuthService")
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/userInfo")
    public ResponseEntity<UserDTO> getCurrentUser() {
        try {
            UserDTO currentUser = userService.getCurrentUser();
            return new ResponseEntity<>(currentUser, HttpStatus.OK);
        } catch (Exception e) {
            log.error(" Exception occurred while getting in {}", ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "success")})
    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        log.info("UserController.registerUser: email={} userName={}", user != null ? user.getEmail() : "null", user != null ? user.getUserName() : "null");
        try {
            String savedUser = this.userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.warn("Registration validation failed: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception occurred while saving user details: {}", ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UsersDTO users, HttpServletRequest request) {
        log.info("UserController.login: email={}", users != null ? users.getEmail() : "null");
        try {
            if (users == null) {
                return new ResponseEntity<>("Request body cannot be null", HttpStatus.BAD_REQUEST);
            }
            String token = this.userService.loginUser(users.getEmail(), users.getPassword(), request);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn("Login failed: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Exception occurred while user login: {}", ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send-otp/{email}")
    public ResponseEntity<?> sendOtp(@PathVariable("email") String email, HttpServletRequest request) {
        log.info("UserController.sendOtp: email={}", email);
        try {
            String result = this.userService.sendOtp(email, request);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn("Send OTP failed: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception occurred while sending OTP: {}", ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verify-otp/{email}/{otp}")
    public ResponseEntity<?> verifyOtp(@PathVariable("email") String email, @PathVariable("otp") int otp) {
        log.info("UserController.verifyOtp: email={}", email);
        try {
            String token = this.userService.verifyOtp(email, otp);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn("Verify OTP failed: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Exception occurred while verifying OTP: {}", ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<String> deleteUser(@Valid @PathVariable long userId) throws Exception {
        log.info(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            this.userService.deleteByUserId(userId);
            return new ResponseEntity<>("User deleted Successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error(" Exception occurred while  user log in{} ", ExceptionUtils.getStackTrace(e));
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/becomeOwner")
    public ResponseEntity<String> becomeOwner(@Valid @RequestBody UserUpdate bean) throws Exception {
        log.info(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            UserDTO currentUser = userService.getCurrentUser();
            userService.updateUser(currentUser.getEmail(), bean);
            log.info("User Controller.login: user : ");
            return new ResponseEntity<>("User Updated Successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error(" Exception occurred  while user log in {}", ExceptionUtils.getStackTrace(e));
            throw new Exception(e.getMessage());
        }
    }

    @PostMapping("/profile-photo")
    public ResponseEntity<String> updateProfilePhoto(@RequestBody byte[] photo) throws Exception {
        try {
            UserDTO currentUser = userService.getCurrentUser();
            userService.updateProfilePhoto(currentUser.getEmail(), photo);
            return new ResponseEntity<>("Profile photo updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while updating profile photo: {}", ExceptionUtils.getStackTrace(e));
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("/profile-photo")
    public ResponseEntity<String> deleteProfilePhoto() throws Exception {
        try {
            UserDTO currentUser = userService.getCurrentUser();
            userService.updateProfilePhoto(currentUser.getEmail(), null);
            return new ResponseEntity<>("Profile photo removed successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while deleting profile photo: {}", ExceptionUtils.getStackTrace(e));
            throw new Exception(e.getMessage());
        }
    }

}
