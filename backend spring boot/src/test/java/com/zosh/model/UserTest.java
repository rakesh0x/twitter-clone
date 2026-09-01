package com.zosh.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User Model Tests")
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    @DisplayName("Should create user with default values")
    void testUserCreation_Default() {
        // Assert
        assertNull(user.getId());
        assertNull(user.getFullName());
        assertNull(user.getEmail());
        assertFalse(user.isReq_user());
        assertFalse(user.isLogin_with_google());
    }

    @Test
    @DisplayName("Should set and get user ID")
    void testSetGetId() {
        // Arrange & Act
        user.setId(1L);

        // Assert
        assertEquals(1L, user.getId());
    }

    @Test
    @DisplayName("Should set and get full name")
    void testSetGetFullName() {
        // Arrange & Act
        user.setFullName("Test User");

        // Assert
        assertEquals("Test User", user.getFullName());
    }

    @Test
    @DisplayName("Should set and get email")
    void testSetGetEmail() {
        // Arrange & Act
        user.setEmail("test@example.com");

        // Assert
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    @DisplayName("Should set and get password")
    void testSetGetPassword() {
        // Arrange & Act
        user.setPassword("password123");

        // Assert
        assertEquals("password123", user.getPassword());
    }

    @Test
    @DisplayName("Should set and get location")
    void testSetGetLocation() {
        // Arrange & Act
        user.setLocation("New York");

        // Assert
        assertEquals("New York", user.getLocation());
    }

    @Test
    @DisplayName("Should set and get website")
    void testSetGetWebsite() {
        // Arrange & Act
        user.setWebsite("https://example.com");

        // Assert
        assertEquals("https://example.com", user.getWebsite());
    }

    @Test
    @DisplayName("Should set and get bio")
    void testSetGetBio() {
        // Arrange & Act
        user.setBio("This is my bio");

        // Assert
        assertEquals("This is my bio", user.getBio());
    }

    @Test
    @DisplayName("Should set and get image")
    void testSetGetImage() {
        // Arrange & Act
        user.setImage("https://example.com/image.jpg");

        // Assert
        assertEquals("https://example.com/image.jpg", user.getImage());
    }

    @Test
    @DisplayName("Should set and get background image")
    void testSetGetBackgroundImage() {
        // Arrange & Act
        user.setBackgroundImage("https://example.com/bg.jpg");

        // Assert
        assertEquals("https://example.com/bg.jpg", user.getBackgroundImage());
    }

    @Test
    @DisplayName("Should initialize followers list")
    void testFollowersList() {
        // Act
        user.setFollowers(new ArrayList<>());

        // Assert
        assertNotNull(user.getFollowers());
        assertEquals(0, user.getFollowers().size());
    }

    @Test
    @DisplayName("Should initialize followings list")
    void testFollowingsList() {
        // Act
        user.setFollowings(new ArrayList<>());

        // Assert
        assertNotNull(user.getFollowings());
        assertEquals(0, user.getFollowings().size());
    }

    @Test
    @DisplayName("Should initialize twit list")
    void testTwitList() {
        // Act
        user.setTwit(new ArrayList<>());

        // Assert
        assertNotNull(user.getTwit());
        assertEquals(0, user.getTwit().size());
    }

    @Test
    @DisplayName("Should initialize likes list")
    void testLikesList() {
        // Act
        user.setLikes(new ArrayList<>());

        // Assert
        assertNotNull(user.getLikes());
        assertEquals(0, user.getLikes().size());
    }

    @Test
    @DisplayName("Should set and get req_user flag")
    void testReqUserFlag() {
        // Arrange & Act
        user.setReq_user(true);

        // Assert
        assertTrue(user.isReq_user());
    }

    @Test
    @DisplayName("Should set and get login_with_google flag")
    void testLoginWithGoogleFlag() {
        // Arrange & Act
        user.setLogin_with_google(true);

        // Assert
        assertTrue(user.isLogin_with_google());
    }

    @Test
    @DisplayName("Should set and get verification")
    void testSetGetVerification() {
        // Arrange
        Varification verification = new Varification();

        // Act
        user.setVerification(verification);

        // Assert
        assertEquals(verification, user.getVerification());
    }

    @Test
    @DisplayName("Should handle mobile number")
    void testSetGetMobile() {
        // Arrange & Act
        user.setMobile("+1234567890");

        // Assert
        assertEquals("+1234567890", user.getMobile());
    }

    @Test
    @DisplayName("Should handle birth date")
    void testSetGetBirthDate() {
        // Arrange & Act
        user.setBirthDate("1990-01-01");

        // Assert
        assertEquals("1990-01-01", user.getBirthDate());
    }

    @Test
    @DisplayName("Should allow adding followers")
    void testAddFollowers() {
        // Arrange
        user.setFollowers(new ArrayList<>());
        User follower = new User();
        follower.setId(2L);

        // Act
        user.getFollowers().add(follower);

        // Assert
        assertEquals(1, user.getFollowers().size());
        assertTrue(user.getFollowers().contains(follower));
    }

    @Test
    @DisplayName("Should allow adding followings")
    void testAddFollowings() {
        // Arrange
        user.setFollowings(new ArrayList<>());
        User following = new User();
        following.setId(2L);

        // Act
        user.getFollowings().add(following);

        // Assert
        assertEquals(1, user.getFollowings().size());
        assertTrue(user.getFollowings().contains(following));
    }
}
