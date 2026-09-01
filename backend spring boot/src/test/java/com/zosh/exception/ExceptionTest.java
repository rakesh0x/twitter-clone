package com.zosh.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Custom Exception Tests")
class ExceptionTest {

    @Test
    @DisplayName("Should create UserException with message")
    void testUserException_WithMessage() {
        // Arrange & Act
        UserException exception = new UserException("User not found");

        // Assert
        assertEquals("User not found", exception.getMessage());
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("Should create TwitException with message")
    void testTwitException_WithMessage() {
        // Arrange & Act
        TwitException exception = new TwitException("Twit not found");

        // Assert
        assertEquals("Twit not found", exception.getMessage());
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("Should create LikeException with message")
    void testLikeException_WithMessage() {
        // Arrange & Act
        LikeException exception = new LikeException("Like not found");

        // Assert
        assertEquals("Like not found", exception.getMessage());
        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("Should throw and catch UserException")
    void testUserException_ThrowAndCatch() {
        // Arrange & Act & Assert
        assertThrows(UserException.class, () -> {
            throw new UserException("Test user error");
        });
    }

    @Test
    @DisplayName("Should throw and catch TwitException")
    void testTwitException_ThrowAndCatch() {
        // Arrange & Act & Assert
        assertThrows(TwitException.class, () -> {
            throw new TwitException("Test twit error");
        });
    }

    @Test
    @DisplayName("Should throw and catch LikeException")
    void testLikeException_ThrowAndCatch() {
        // Arrange & Act & Assert
        assertThrows(LikeException.class, () -> {
            throw new LikeException("Test like error");
        });
    }

    @Test
    @DisplayName("Should get exception message from UserException")
    void testUserException_GetMessage() {
        // Arrange
        String expectedMessage = "Custom user error message";

        // Act
        UserException exception = new UserException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Should verify UserException extends Exception")
    void testUserException_ExtendsException() {
        // Arrange & Act
        UserException exception = new UserException("Test");

        // Assert
        assertIsInstance(Exception.class, exception);
    }

    @Test
    @DisplayName("Should verify TwitException extends Exception")
    void testTwitException_ExtendsException() {
        // Arrange & Act
        TwitException exception = new TwitException("Test");

        // Assert
        assertIsInstance(Exception.class, exception);
    }

    @Test
    @DisplayName("Should verify LikeException extends Exception")
    void testLikeException_ExtendsException() {
        // Arrange & Act
        LikeException exception = new LikeException("Test");

        // Assert
        assertIsInstance(Exception.class, exception);
    }

    /**
     * Helper method to check instance relationship
     */
    private <T> void assertIsInstance(Class<T> expectedClass, Object actual) {
        assertTrue(expectedClass.isInstance(actual));
    }
}
