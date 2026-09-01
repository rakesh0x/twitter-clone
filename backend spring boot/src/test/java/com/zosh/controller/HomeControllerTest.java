package com.zosh.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zosh.response.ApiResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("HomeController Tests")
class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    @Test
    @DisplayName("Should return welcome message with HTTP 202 ACCEPTED")
    void testHomeController_Success() {
        // Act
        ResponseEntity<ApiResponse> response = homeController.homeController();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Welcome To Twitter Api", response.getBody().getMessage());
        assertTrue(response.getBody().isStatus());
    }

    @Test
    @DisplayName("Should return response with correct structure")
    void testHomeController_ResponseStructure() {
        // Act
        ResponseEntity<ApiResponse> response = homeController.homeController();

        // Assert
        assertNotNull(response.getBody());
        ApiResponse body = response.getBody();
        assertNotNull(body.getMessage());
        assertTrue(body.isStatus());
    }
}
