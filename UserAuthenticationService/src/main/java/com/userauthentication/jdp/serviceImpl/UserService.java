package com.userauthentication.jdp.serviceImpl;

import com.userauthentication.jdp.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    String saveUser(User user) throws Exception;

    User updateUser(User user , Long userId);

    String loginUser(String email, String password, HttpServletRequest request) throws Exception;

    String deleteByUserId(Long userId) throws Exception;

    void updateUserPassword(Long userId, String password);

    void updateUserRole(Long userId, String role);

}
