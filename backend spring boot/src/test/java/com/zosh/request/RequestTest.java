package com.zosh.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LoginRequest Tests")
class LoginRequestTest {

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
    }

    @Test
    @DisplayName("Should create LoginRequest with default constructor")
    void testLoginRequest_DefaultConstructor() {
        // Assert
        assertNull(loginRequest.getEmail());
        assertNull(loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should create LoginRequest with all arguments constructor")
    void testLoginRequest_AllArgsConstructor() {
        // Arrange & Act
        LoginRequest request = new LoginRequest("testuser@example.com", "password123");

        // Assert
        assertEquals("testuser@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
    }

    @Test
    @DisplayName("Should set and get email")
    void testSetGetEmail() {
        // Arrange & Act
        loginRequest.setEmail("user@example.com");

        // Assert
        assertEquals("user@example.com", loginRequest.getEmail());
    }

    @Test
    @DisplayName("Should set and get password")
    void testSetGetPassword() {
        // Arrange & Act
        loginRequest.setPassword("securePassword123!");

        // Assert
        assertEquals("securePassword123!", loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should set both email and password")
    void testSetEmailAndPassword() {
        // Arrange & Act
        loginRequest.setEmail("admin@example.com");
        loginRequest.setPassword("adminPass456");

        // Assert
        assertEquals("admin@example.com", loginRequest.getEmail());
        assertEquals("adminPass456", loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle empty email")
    void testEmptyEmail() {
        // Arrange & Act
        loginRequest.setEmail("");
        loginRequest.setPassword("password123");

        // Assert
        assertEquals("", loginRequest.getEmail());
        assertEquals("password123", loginRequest.getPassword());
    }

    @Test
    @DisplayName("Should handle null password")
    void testNullPassword() {
        // Arrange & Act
        loginRequest.setEmail("user@example.com");
        loginRequest.setPassword(null);

        // Assert
        assertEquals("user@example.com", loginRequest.getEmail());
        assertNull(loginRequest.getPassword());
    }
}

@DisplayName("TwitRequest Tests")
class TwitRequestTest {

    private TwitRequest twitRequest;

    @BeforeEach
    void setUp() {
        twitRequest = new TwitRequest();
    }

    @Test
    @DisplayName("Should create TwitRequest with default constructor")
    void testTwitRequest_DefaultConstructor() {
        // Assert
        assertNotNull(twitRequest);
    }

    @Test
    @DisplayName("Should handle TwitRequest creation")
    void testTwitRequest_Creation() {
        // Arrange
        TwitRequest request = new TwitRequest();

        // Assert
        assertNotNull(request);
    }
}

@DisplayName("TwitReplyRequest Tests")
class TwitReplyRequestTest {

    private TwitReplyRequest replyRequest;

    @BeforeEach
    void setUp() {
        replyRequest = new TwitReplyRequest();
    }

    @Test
    @DisplayName("Should create TwitReplyRequest with default constructor")
    void testTwitReplyRequest_DefaultConstructor() {
        // Assert
        assertNotNull(replyRequest);
    }

    @Test
    @DisplayName("Should handle TwitReplyRequest creation")
    void testTwitReplyRequest_Creation() {
        // Arrange
        TwitReplyRequest request = new TwitReplyRequest();

        // Assert
        assertNotNull(request);
    }
}

@DisplayName("LoginWithGooleRequest Tests")
class LoginWithGooleRequestTest {

    private LoginWithGooleRequest googleRequest;

    @BeforeEach
    void setUp() {
        googleRequest = new LoginWithGooleRequest();
    }

    @Test
    @DisplayName("Should create LoginWithGooleRequest with default constructor")
    void testLoginWithGooleRequest_DefaultConstructor() {
        // Assert
        assertNotNull(googleRequest);
    }

    @Test
    @DisplayName("Should handle LoginWithGooleRequest creation")
    void testLoginWithGooleRequest_Creation() {
        // Arrange
        LoginWithGooleRequest request = new LoginWithGooleRequest();

        // Assert
        assertNotNull(request);
    }
}
