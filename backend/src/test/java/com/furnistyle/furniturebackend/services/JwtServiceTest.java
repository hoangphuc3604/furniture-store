package com.furnistyle.furniturebackend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Claims;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JwtServiceTest {
    @Autowired
    private JwtService jwtService;

    @Value("${application.security.jwt.secret-key}")
    private String secretKeyString;

    @Value("${application.security.jwt.expiration}")
    private long expiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    private final String username = "testUser";

    @Test
    void extractUsername_WhenTokenIsValid_ThenReturnUsername() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            username,
            "encodedPassword",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtService.generateToken(userDetails);

        // Act
        String extractedUsername = jwtService.extractUsername(token);

        // Assert
        assertEquals(username, extractedUsername);
    }

    @Test
    void generateToken_WhenCalled_ThenReturnJwtToken() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            username,
            "encodedPassword",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertEquals(3, token.split("\\.").length); // JWT has 3 parts: header, payload, signature
    }

    @Test
    void isTokenValid_WhenTokenIsValid_ThenReturnTrue() {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            username,
            "encodedPassword",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void extractClaim_WhenTokenIsValid_ThenReturnClaim() {
        // Arrange
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            username,
            "encodedPassword",
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtService.generateToken(userDetails);

        // Act
        String claim = jwtService.extractClaim(token, Claims::getSubject);

        // Assert
        assertEquals(username, claim);
    }
}
