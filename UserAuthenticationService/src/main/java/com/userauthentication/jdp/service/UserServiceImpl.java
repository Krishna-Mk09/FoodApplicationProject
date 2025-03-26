package com.userauthentication.jdp.service;

import com.foodapplication.jdp.Common_Service.Service.SequenceService;
import com.userauthentication.jdp.config.SecurityConfig;
import com.userauthentication.jdp.entity.User;
import com.userauthentication.jdp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SecurityConfig encoder;
    private final SecurityTokenGeneratorImpl SECURITY_TOKEN_GENERATOR;

    @Autowired
    private SequenceService sequenceService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, SecurityConfig passwordEncoder, SecurityTokenGeneratorImpl securityTokenGenerator) {
        this.userRepository = userRepository;
        this.encoder = passwordEncoder;
        SECURITY_TOKEN_GENERATOR = securityTokenGenerator;
    }


    @Override
    public String saveUser(User user) throws Exception {
        String status = null;
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            status = "User details are incomplete";
            return status;
        }
        if (userRepository.findByUserName(user.getUserName()) != null) {
            status = "UserName Already Exists.Please Choose Another UserName";
            return status;
        }
        if (userRepository.existsByEmailOrPhoneNum(user.getEmail(), user.getPhoneNum()) || this.userRepository.existsById(user.getUserId())) {
            status = "User already exists with the given email, phone or ID";
            return status;
        }
        try {
            if (user.getUserId() == 0L) {
                long userSeqId = sequenceService.getSequenceByCustomer("USERS", user.getUserId());
                user.setUserId(userSeqId);
            } else {
                log.error("Sequence generation failed for USERS Table");
            }
            user.setRole("USER");
            user.setCreatedBy(user.getEmail());
            user.setCreationDate(java.time.LocalDateTime.now());
            user.setIsActive(true);
            user.setExpDate(java.time.LocalDateTime.now().plusYears(20));
            user.setPassword(encoder.passwordEncoder().encode(user.getPassword()));
            user.setNoOfLoginAttempts(0);
            user.setFirstName(user.getEmail().substring(0, user.getEmail().indexOf("@")));
            userRepository.save(user);
            status = "User saved successfully";
            log.info("User saved successfully");
            return status;
        } catch (Exception e) {
            log.error("Exception occurred while saving user details", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public User updateUser(User user, Long userId) {
        return null;
    }


    @Override
    public String loginUser(String email, String password) throws Exception {
        String token = null;
        try {
            if (email == null || password == null) {
                token = "Email and password must not be null";
                return token;
            }
            User user = userRepository.findByEmail(email);
            if (user == null) {
                token = "User not found. Please Register";
                return token;
            }
            if (user != null && encoder.passwordEncoder().matches(password, user.getPassword())) {
                token = this.SECURITY_TOKEN_GENERATOR.generateToken(user);
                log.info("User token generated and returned");
                return new ResponseEntity<>(token, HttpStatus.OK).getBody();
            }
        } catch (Exception e) {
            log.error("Exception occurred while logging in", ExceptionUtils.getStackTrace(e));
            throw e;
        }
        return token;
    }


    @Override
    public String deleteByUserId(Long userId) throws Exception {
        if (userId == null) {
            throw new Exception("User Id must not be null");
        }
        String user = userRepository.deleteByUserId(userId);
        return user;
    }

    @Override
    @Transactional
    public void updateUserPassword(Long userId, String newPassword) {
        if (userId == null || newPassword == null) {
            throw new IllegalArgumentException("User Id and new password must not be null");
        }
        userRepository.updateUserPassword(userId, newPassword);
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, String role) {
        userRepository.updateUserRole(userId, role);
    }
}
