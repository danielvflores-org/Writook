package com.danielvflores.writook.utility;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class TokenJWTUtility {
    private static final String SECRET_KEY = "my-super-secret-key-which-should-be-long-and-random-1234567890";
    private static final long EXPIRATION_TIME = 86400000;

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }


    public static String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    public static String getUsernameFromToken(String token) {
        try {
            System.out.println("Intentando extraer username del token: " + token);
            String username = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            System.out.println("Username extraído exitosamente: " + username);
            return username;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Error al extraer username del token: " + e.getMessage());
            return null;
        }
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}