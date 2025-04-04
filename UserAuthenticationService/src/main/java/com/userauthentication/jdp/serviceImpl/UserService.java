package com.userauthentication.jdp.serviceImpl;

import com.userauthentication.jdp.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    String saveUser(User user) throws Exception;

    String loginUser(String email, String password, HttpServletRequest request) throws Exception;

    String sendOtp(String email, HttpServletRequest request) throws Exception;

    String verifyOtp(String email, int otp) throws Exception;

    @Transactional
    @Modifying
    void deleteByUserId(long userId) throws Exception;

    @Transactional
    @Modifying
    void updateUserPassword(long userId, String newPassword);

    @Transactional
    void updateUserRole(long userId, String role);
}
