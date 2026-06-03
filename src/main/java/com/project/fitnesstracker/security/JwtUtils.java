package com.project.fitnesstracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;


@Component

public class JwtUtils {
    private String jwtSecret = "mySuperSecretKeyForJwtAuthentication1234567890";
    private int jwtExpirationInMs = 1860000;


    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) return bearerToken.substring(7);
        return null;
    }


    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() +jwtExpirationInMs))
                .signWith(keys()).
                compact();
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) keys())
                    .build()
                    .parseSignedClaims(jwtToken);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Key keys() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }


    public String genrateToken(Long userId, String role) {

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("roles", List.of(role))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(keys()).compact();
    }

    public String getUserIdFromToken(String jwt) {

        return Jwts.parser().verifyWith((SecretKey) keys())
                .build().parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    public Claims getAllClaims(String jwt) {

        return Jwts.parser()
                .verifyWith((SecretKey) keys())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}

