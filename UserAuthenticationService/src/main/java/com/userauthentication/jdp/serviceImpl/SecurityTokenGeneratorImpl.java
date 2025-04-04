package com.userauthentication.jdp.serviceImpl;

import com.userauthentication.jdp.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@Slf4j
public class SecurityTokenGeneratorImpl implements SecurityTokenGenerator {
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;
    private static String secretKey="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

    @Override
    public String generateToken(User user) {
        return Jwts.builder().setSubject(user.getEmail()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).claim("role", user.getRole()).claim("isActive", user.getIsActive()).claim("username", user.getUserName()).signWith(SECRET_KEY, SignatureAlgorithm.HS256).compact();
    }
}
