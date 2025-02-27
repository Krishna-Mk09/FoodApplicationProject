package com.userauthentication.jdp.service;

import com.userauthentication.jdp.entity.User;

import java.util.Optional;

public interface UserService {

    User saveUser(User user) throws Exception;

    User updateUser(User user , Long userId);

    void loginUser(String email, String password) throws Exception;

    void deleteByUserId(Long userId);

    void updateUserPassword(Long userId, String password);

    void updateUserRole(Long userId, String role);

}
