package com.ide.api.configurations;

import com.ide.api.entities.CustomUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    // Load the key from a secure location or use a secure key generation mechanism
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof CustomUser)) {
            throw new IllegalArgumentException("Unsupported principal type");
        }

        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", customUser.getUsername());
        claims.put("isAdmin", customUser.isAdmin());

        // Generate JWT token with user details and expiration date
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
