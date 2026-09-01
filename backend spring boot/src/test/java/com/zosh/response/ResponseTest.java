package com.zosh.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ApiResponse Tests")
class ApiResponseTest {

    private ApiResponse apiResponse;

    @BeforeEach
    void setUp() {
        apiResponse = new ApiResponse();
    }

    @Test
    @DisplayName("Should create ApiResponse with default constructor")
    void testApiResponse_DefaultConstructor() {
        // Assert
        assertNull(apiResponse.getMessage());
        assertFalse(apiResponse.isStatus());
    }

    @Test
    @DisplayName("Should create ApiResponse with all arguments constructor")
    void testApiResponse_AllArgsConstructor() {
        // Arrange & Act
        ApiResponse response = new ApiResponse("Success", true);

        // Assert
        assertEquals("Success", response.getMessage());
        assertTrue(response.isStatus());
    }

    @Test
    @DisplayName("Should set and get message")
    void testSetGetMessage() {
        // Arrange & Act
        apiResponse.setMessage("Operation successful");

        // Assert
        assertEquals("Operation successful", apiResponse.getMessage());
    }

    @Test
    @DisplayName("Should set and get status")
    void testSetGetStatus() {
        // Arrange & Act
        apiResponse.setStatus(true);

        // Assert
        assertTrue(apiResponse.isStatus());
    }

    @Test
    @DisplayName("Should handle false status")
    void testSetGetStatus_False() {
        // Arrange & Act
        apiResponse.setStatus(false);

        // Assert
        assertFalse(apiResponse.isStatus());
    }

    @Test
    @DisplayName("Should update message and status")
    void testUpdateMessageAndStatus() {
        // Arrange & Act
        apiResponse.setMessage("Updated message");
        apiResponse.setStatus(true);

        // Assert
        assertEquals("Updated message", apiResponse.getMessage());
        assertTrue(apiResponse.isStatus());
    }
}

@DisplayName("AuthResponse Tests")
class AuthResponseTest {

    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        authResponse = new AuthResponse();
    }

    @Test
    @DisplayName("Should create AuthResponse with default constructor")
    void testAuthResponse_DefaultConstructor() {
        // Assert
        assertNull(authResponse.getJwt());
        assertFalse(authResponse.isStatus());
    }

    @Test
    @DisplayName("Should create AuthResponse with all arguments constructor")
    void testAuthResponse_AllArgsConstructor() {
        // Arrange & Act
        AuthResponse response = new AuthResponse("valid.jwt.token", true);

        // Assert
        assertEquals("valid.jwt.token", response.getJwt());
        assertTrue(response.isStatus());
    }

    @Test
    @DisplayName("Should set and get JWT token")
    void testSetGetJwt() {
        // Arrange & Act
        authResponse.setJwt("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");

        // Assert
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", authResponse.getJwt());
    }

    @Test
    @DisplayName("Should set and get status")
    void testSetGetStatus() {
        // Arrange & Act
        authResponse.setStatus(true);

        // Assert
        assertTrue(authResponse.isStatus());
    }

    @Test
    @DisplayName("Should set JWT and status together")
    void testSetJwtAndStatus() {
        // Arrange & Act
        authResponse.setJwt("valid.token");
        authResponse.setStatus(true);

        // Assert
        assertEquals("valid.token", authResponse.getJwt());
        assertTrue(authResponse.isStatus());
    }

    @Test
    @DisplayName("Should handle failed authentication response")
    void testFailedAuthResponse() {
        // Arrange & Act
        authResponse.setJwt(null);
        authResponse.setStatus(false);

        // Assert
        assertNull(authResponse.getJwt());
        assertFalse(authResponse.isStatus());
    }
}
