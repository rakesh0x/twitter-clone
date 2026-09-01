package com.zosh.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zosh.config.JwtProvider;
import com.zosh.exception.UserException;
import com.zosh.model.User;
import com.zosh.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImplementation Tests")
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserServiceImplementation userService;

    private User testUser;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        testUser = new User();
        testUser.setId(testUserId);
        testUser.setFullName("Test User");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password123");
        testUser.setLocation("Test Location");
        testUser.setWebsite("https://example.com");
        testUser.setBio("Test Bio");
    }

    @Test
    @DisplayName("Should find user by ID successfully")
    void testFindUserById_Success() throws UserException {
        // Arrange
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.findUserById(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(testUserId, result.getId());
        assertEquals("Test User", result.getFullName());
        assertEquals("testuser@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(testUserId);
    }

    @Test
    @DisplayName("Should throw UserException when user not found by ID")
    void testFindUserById_NotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserException.class, () -> userService.findUserById(testUserId));
        verify(userRepository, times(1)).findById(testUserId);
    }

    @Test
    @DisplayName("Should find user profile by JWT successfully")
    void testFindUserProfileByJwt_Success() throws UserException {
        // Arrange
        String jwt = "Bearer valid.jwt.token";
        when(jwtProvider.getEmailFromJwtToken(jwt)).thenReturn("testuser@example.com");
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(testUser);

        // Act
        User result = userService.findUserProfileByJwt(jwt);

        // Assert
        assertNotNull(result);
        assertEquals("testuser@example.com", result.getEmail());
        verify(jwtProvider, times(1)).getEmailFromJwtToken(jwt);
        verify(userRepository, times(1)).findByEmail("testuser@example.com");
    }

    @Test
    @DisplayName("Should throw UserException when user not found by email from JWT")
    void testFindUserProfileByJwt_UserNotFound() {
        // Arrange
        String jwt = "Bearer valid.jwt.token";
        when(jwtProvider.getEmailFromJwtToken(jwt)).thenReturn("nonexistent@example.com");
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert
        assertThrows(UserException.class, () -> userService.findUserProfileByJwt(jwt));
        verify(jwtProvider, times(1)).getEmailFromJwtToken(jwt);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should update user successfully with all fields")
    void testUpdateUser_Success() throws UserException {
        // Arrange
        User updateRequest = new User();
        updateRequest.setFullName("Updated Name");
        updateRequest.setImage("https://example.com/image.jpg");
        updateRequest.setBackgroundImage("https://example.com/bg.jpg");
        updateRequest.setBirthDate("1990-01-01");
        updateRequest.setLocation("Updated Location");
        updateRequest.setBio("Updated Bio");
        updateRequest.setWebsite("https://updated.com");

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.updateUser(testUserId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getFullName());
        assertEquals("https://example.com/image.jpg", result.getImage());
        assertEquals("https://example.com/bg.jpg", result.getBackgroundImage());
        assertEquals("1990-01-01", result.getBirthDate());
        assertEquals("Updated Location", result.getLocation());
        assertEquals("Updated Bio", result.getBio());
        assertEquals("https://updated.com", result.getWebsite());
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should update user with partial fields")
    void testUpdateUser_PartialUpdate() throws UserException {
        // Arrange
        User updateRequest = new User();
        updateRequest.setFullName("Partial Updated Name");

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.updateUser(testUserId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Partial Updated Name", result.getFullName());
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should throw UserException when updating non-existent user")
    void testUpdateUser_UserNotFound() {
        // Arrange
        User updateRequest = new User();
        updateRequest.setFullName("Updated Name");

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserException.class, () -> userService.updateUser(testUserId, updateRequest));
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should follow user successfully")
    void testFollowUser_Success() throws UserException {
        // Arrange
        User followingUser = new User();
        followingUser.setId(2L);
        followingUser.setFullName("Following User");
        followingUser.setFollowings(new ArrayList<>());
        followingUser.setFollowers(new ArrayList<>());

        User userToFollow = new User();
        userToFollow.setId(testUserId);
        userToFollow.setFullName("User To Follow");
        userToFollow.setFollowers(new ArrayList<>());
        userToFollow.setFollowings(new ArrayList<>());

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(userToFollow));
        when(userRepository.save(any(User.class))).thenReturn(userToFollow);

        // Act
        User result = userService.followUser(testUserId, followingUser);

        // Assert
        assertNotNull(result);
        assertTrue(userToFollow.getFollowers().contains(followingUser));
        assertTrue(followingUser.getFollowings().contains(userToFollow));
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    @DisplayName("Should unfollow user successfully")
    void testFollowUser_Unfollow() throws UserException {
        // Arrange
        User followingUser = new User();
        followingUser.setId(2L);
        followingUser.setFollowings(new ArrayList<>());
        followingUser.setFollowers(new ArrayList<>());

        User userToUnfollow = new User();
        userToUnfollow.setId(testUserId);
        userToUnfollow.setFollowers(new ArrayList<>());
        userToUnfollow.setFollowings(new ArrayList<>());
        
        // Pre-populate the follow relationship
        userToUnfollow.getFollowers().add(followingUser);
        followingUser.getFollowings().add(userToUnfollow);

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(userToUnfollow));
        when(userRepository.save(any(User.class))).thenReturn(userToUnfollow);

        // Act
        User result = userService.followUser(testUserId, followingUser);

        // Assert
        assertNotNull(result);
        assertFalse(userToUnfollow.getFollowers().contains(followingUser));
        assertFalse(followingUser.getFollowings().contains(userToUnfollow));
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    @DisplayName("Should search users successfully")
    void testSearchUser_Success() {
        // Arrange
        List<User> searchResults = new ArrayList<>();
        searchResults.add(testUser);
        
        User user2 = new User();
        user2.setId(2L);
        user2.setFullName("Test User 2");
        user2.setEmail("testuser2@example.com");
        searchResults.add(user2);

        when(userRepository.searchUser("Test")).thenReturn(searchResults);

        // Act
        List<User> results = userService.searchUser("Test");

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(userRepository, times(1)).searchUser("Test");
    }

    @Test
    @DisplayName("Should return empty list when search yields no results")
    void testSearchUser_NoResults() {
        // Arrange
        when(userRepository.searchUser("NonExistent")).thenReturn(new ArrayList<>());

        // Act
        List<User> results = userService.searchUser("NonExistent");

        // Assert
        assertNotNull(results);
        assertEquals(0, results.size());
        verify(userRepository, times(1)).searchUser("NonExistent");
    }
}
