package com.userauthentication.jdp.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Author Name : M.V.Krishna
 * Date: 03-04-2025
 * Created With: IntelliJ IDEA Ultimate Edition
 */
@Slf4j
public class JwtFilter extends GenericFilterBean {
    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();
        if (path.matches("^/userAuthService/registerUser$") || path.matches("^/userAuthService/login$") || path.matches("^/userAuthService/send-otp/.*$") || path.matches("^/userAuthService/verify-otp/.*$") || path.matches("^/userAuthService/google/.*$") || path.matches("^/google/.*$") || path.matches("^/v3/api-docs/.*$") || path.matches("^/swagger-ui/.*$")) {
            chain.doFilter(request, response);
            return;
        }
        String authHeader = httpRequest.getHeader("Authorization");
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }
        String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            List<GrantedAuthority> authorities = Arrays.stream(claims.get("role", String.class).split(",")).map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (SignatureException e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature");
        } catch (Exception e) {
            log.error("Exception occurred while validating token: {}", e.getMessage(), e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
        }
    }
}
