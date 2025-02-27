package com.userauthentication.jdp.service;

import com.userauthentication.jdp.entity.User;

import java.util.Map;

/*
 * Author Name : M.V.Krishna
 * Date: 27-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
public interface SecurityTokenGenerator {

    public Map<String, String> generateToken(User user);

}
