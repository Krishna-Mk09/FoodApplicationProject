package com.userauthentication.jdp.service;

import com.userauthentication.jdp.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * Author Name : M.V.Krishna
 * Date: 27-02-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */

@Service
public class SecurityTokenGeneratorImpl implements SecurityTokenGenerator {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Override
    public Map<String, String> generateToken(User user) {
        String jwtToken = Jwts.builder().setSubject(user.getEmail()).
                setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.ES256, secretKey).compact();

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("message", "User successfully logged in");
        return response;
    }
}
