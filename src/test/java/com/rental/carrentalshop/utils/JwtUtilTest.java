package com.rental.carrentalshop.utils;

import com.rental.carrentalshop.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void shouldGenerateValidToken() {
        String token = jwtUtil.generateToken("testuser", "USER");

        assertThat(token).isNotNull();
        assertThat(jwtUtil.isTokenValid(token)).isTrue();
        assertThat(jwtUtil.validateToken(token)).isEqualTo("testuser");
        assertThat(jwtUtil.extractRole(token)).isEqualTo("USER");
    }

    @Test
    void shouldInvalidateIncorrectToken() {
        String fakeToken = "invalid.token.string";

        assertThat(jwtUtil.isTokenValid(fakeToken)).isFalse();
        assertThat(jwtUtil.validateToken(fakeToken)).isNull();
    }

    @Test
    void shouldRevokeToken() {
        String token = jwtUtil.generateToken("testuser", "USER");
        assertThat(jwtUtil.isTokenValid(token)).isTrue();

        jwtUtil.revokeUserToken("testuser");

        assertThat(jwtUtil.isTokenValid(token)).isFalse();
        assertThat(jwtUtil.validateToken(token)).isNull();
    }

    @Test
    void shouldRevokeAllTokens() {
        String token1 = jwtUtil.generateToken("user1", "USER");
        String token2 = jwtUtil.generateToken("admin1", "ADMIN");

        assertThat(jwtUtil.isTokenValid(token1)).isTrue();
        assertThat(jwtUtil.isTokenValid(token2)).isTrue();

        jwtUtil.revokeAllTokens();

        assertThat(jwtUtil.isTokenValid(token1)).isFalse();
        assertThat(jwtUtil.isTokenValid(token2)).isFalse();
    }

    @Test
    void shouldNotValidateTokenAfterRevoking() {
        String token = jwtUtil.generateToken("testuser", "USER");
        assertThat(jwtUtil.isTokenValid(token)).isTrue();

        jwtUtil.revokeUserToken("testuser");

        assertThat(jwtUtil.isTokenValid(token)).isFalse();
        assertThat(jwtUtil.validateToken(token)).isNull();
    }
}
