package com.rental.carrentalshop.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key";
    private static final long EXPIRATION_TIME = 86400000; // 1 day (in milliseconds)

    private final Map<String, String> userTokens = new ConcurrentHashMap<>();

    public String generateToken(String username, String role) {
        String token = JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));

        userTokens.put(username, token);
        return token;
    }

    public boolean isTokenValid(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
            DecodedJWT jwt = verifier.verify(token);
            String username = jwt.getSubject();

            return userTokens.containsKey(username) && token.equals(userTokens.get(username));
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
            DecodedJWT jwt = verifier.verify(token);
            String username = jwt.getSubject();

            if (!userTokens.containsKey(username) || !token.equals(userTokens.get(username))) {
                return null;
            }
            return username;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String extractRole(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("role").asString();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public void revokeUserToken(String username) {
        userTokens.remove(username);
    }

    public void revokeAllTokens() {
        userTokens.clear();
    }
}
