package com.zosh.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.zosh.model.User;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFullName("Test User");
        testUser.setEmail("testuser@example.com");
        testUser = userRepository.save(testUser);
    }

    @Test
    @DisplayName("Should find user by email successfully")
    void testFindByEmail_Success() {
        // Act
        User result = userRepository.findByEmail("testuser@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("testuser@example.com", result.getEmail());
        assertEquals("Test User", result.getFullName());
    }

    @Test
    @DisplayName("Should return null when user not found by email")
    void testFindByEmail_NotFound() {
        // Act
        User result = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should find user by ID successfully")
    void testFindById_Success() {
        // Act
        Optional<User> result = userRepository.findById(testUser.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser.getId(), result.get().getId());
        assertEquals("testuser@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty Optional when user not found by ID")
    void testFindById_NotFound() {
        // Act
        Optional<User> result = userRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should search users by full name")
    void testSearchUser_ByFullName() {
        // Arrange
        User user2 = new User();
        user2.setFullName("Test Admin");
        user2.setEmail("admin@example.com");
        userRepository.save(user2);

        // Act
        List<User> results = userRepository.searchUser("Test");

        // Assert
        assertNotNull(results);
        assertTrue(results.size() >= 1);
        assertTrue(results.stream().anyMatch(u -> u.getFullName().contains("Test")));
    }

    @Test
    @DisplayName("Should search users by email")
    void testSearchUser_ByEmail() {
        // Arrange
        User user2 = new User();
        user2.setFullName("Test Admin");
        user2.setEmail("admin@example.com");
        userRepository.save(user2);

        // Act
        List<User> results = userRepository.searchUser("admin");

        // Assert
        assertNotNull(results);
        assertTrue(results.size() >= 1);
        assertTrue(results.stream().anyMatch(u -> u.getEmail().contains("admin")));
    }

    @Test
    @DisplayName("Should return empty list when search yields no results")
    void testSearchUser_NoResults() {
        // Act
        List<User> results = userRepository.searchUser("NonExistentUser");

        // Assert
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    @DisplayName("Should search users with case-insensitive query")
    void testSearchUser_CaseInsensitive() {
        // Act
        List<User> results = userRepository.searchUser("test");

        // Assert
        assertNotNull(results);
        assertTrue(results.stream().anyMatch(u -> u.getFullName().equalsIgnoreCase("Test User")));
    }

    @Test
    @DisplayName("Should save user successfully")
    void testSave_Success() {
        // Arrange
        User newUser = new User();
        newUser.setFullName("New User");
        newUser.setEmail("newuser@example.com");

        // Act
        User savedUser = userRepository.save(newUser);

        // Assert
        assertNotNull(savedUser.getId());
        assertEquals("newuser@example.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdate_Success() {
        // Arrange
        testUser.setFullName("Updated Name");
        testUser.setBio("Updated Bio");

        // Act
        User updatedUser = userRepository.save(testUser);

        // Assert
        assertEquals("Updated Name", updatedUser.getFullName());
        assertEquals("Updated Bio", updatedUser.getBio());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void testDelete_Success() {
        // Arrange
        Long userId = testUser.getId();

        // Act
        userRepository.deleteById(userId);

        // Assert
        assertFalse(userRepository.findById(userId).isPresent());
    }

    @Test
    @DisplayName("Should enforce unique email constraint")
    void testSave_UniqueEmailConstraint() {
        // Arrange
        User duplicateUser = new User();
        duplicateUser.setFullName("Duplicate User");
        duplicateUser.setEmail("testuser@example.com");

        // Act & Assert
        assertThrows(Exception.class, () -> userRepository.save(duplicateUser));
    }
}
