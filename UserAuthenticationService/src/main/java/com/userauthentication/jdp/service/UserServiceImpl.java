package com.userauthentication.jdp.service;

import com.userauthentication.jdp.config.SecurityConfig;
import com.userauthentication.jdp.entity.User;
import com.userauthentication.jdp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SecurityConfig encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SecurityConfig passwordEncoder) {
        this.userRepository = userRepository;
        this.encoder = passwordEncoder;
    }


    @Override
    public User saveUser(User user) throws Exception {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            throw new Exception("User details are incomplete");
        }
        if (this.userRepository.existsById(user.getUserId())) {
            throw new Exception("User already exists");
        }
        try {
            user.setPassword(encoder.passwordEncoder().encode(user.getPassword()));
            return userRepository.save(user);
        } catch (Exception e) {
            //  log.error("Exception occurred while saving user details", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }

    @Override
    public User updateUser(User user, Long userId) {
        return null;
    }


    @Override
    public void loginUser(String email, String password) throws Exception {
        try {
            if (email == null || password == null) {
                throw new Exception("Email and password must not be null");
            }

            User user = userRepository.findByEmail(email);
            if (user == null || !encoder.passwordEncoder().matches(password, user.getPassword())) {
                throw new Exception("Invalid email or password");
            }
        } catch (Exception e) {
            //  log.error("Exception occurred while logging in", ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }


    @Override
    public void deleteByUserId(Long userId) throws Exception {
        if (userId == null) {
            throw new Exception("User Id must not be null");
        }
        userRepository.deleteByUserId(userId);
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
