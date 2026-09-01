package com.zosh.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtProvider Tests")
class JwtProviderTest {

    @InjectMocks
    private JwtProvider jwtProvider;

    @Mock
    private Authentication auth;

    private SecretKey key;

    @BeforeEach
    void setUp() {
        key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void testGenerateToken_Success() {
        // Arrange
        String email = "testuser@example.com";
        when(auth.getName()).thenReturn(email);

        // Act
        String token = jwtProvider.generateToken(auth);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > 0);
        verify(auth, times(1)).getName();
    }

    @Test
    @DisplayName("Should extract email from valid JWT token")
    void testGetEmailFromJwtToken_Success() {
        // Arrange
        String email = "testuser@example.com";
        String token = Jwts.builder()
                .claim("email", email)
                .signWith(key)
                .compact();
        String bearerToken = "Bearer " + token;

        // Act
        String extractedEmail = jwtProvider.getEmailFromJwtToken(bearerToken);

        // Assert
        assertNotNull(extractedEmail);
        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("Should throw exception for invalid JWT token")
    void testGetEmailFromJwtToken_InvalidToken() {
        // Arrange
        String invalidToken = "Bearer invalid.token.here";

        // Act & Assert
        assertThrows(Exception.class, () -> jwtProvider.getEmailFromJwtToken(invalidToken));
    }

    @Test
    @DisplayName("Should populate authorities correctly")
    void testPopulateAuthorities_SingleAuthority() {
        // Arrange
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Act
        String result = jwtProvider.populateAuthorities(authorities);

        // Assert
        assertNotNull(result);
        assertEquals("ROLE_USER", result);
    }

    @Test
    @DisplayName("Should populate multiple authorities correctly")
    void testPopulateAuthorities_MultipleAuthorities() {
        // Arrange
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Act
        String result = jwtProvider.populateAuthorities(authorities);

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("ROLE_USER"));
        assertTrue(result.contains("ROLE_ADMIN"));
        assertTrue(result.contains(","));
    }

    @Test
    @DisplayName("Should handle empty authorities collection")
    void testPopulateAuthorities_Empty() {
        // Arrange
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // Act
        String result = jwtProvider.populateAuthorities(authorities);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle null authorities collection")
    void testPopulateAuthorities_Null() {
        // Act & Assert
        // This test verifies the method handles null gracefully
        // The actual behavior depends on implementation
        Collection<GrantedAuthority> authorities = null;
        assertThrows(NullPointerException.class, () -> jwtProvider.populateAuthorities(authorities));
    }
}
