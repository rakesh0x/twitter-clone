package com.zosh.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.zosh.dto.UserDto;
import com.zosh.dto.mapper.UserDtoMapper;
import com.zosh.exception.UserException;
import com.zosh.model.User;
import com.zosh.service.UserService;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserDto testUserDto;
    private Long testUserId;
    private String jwt;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        jwt = "Bearer valid.jwt.token";

        testUser = new User();
        testUser.setId(testUserId);
        testUser.setFullName("Test User");
        testUser.setEmail("testuser@example.com");
        testUser.setLocation("Test Location");
        testUser.setWebsite("https://example.com");
        testUser.setBio("Test Bio");
        testUser.setFollowers(new ArrayList<>());
        testUser.setFollowings(new ArrayList<>());

        testUserDto = new UserDto();
        testUserDto.setId(testUserId);
        testUserDto.setFullName("Test User");
        testUserDto.setEmail("testuser@example.com");
    }

    @Test
    @DisplayName("Should get user profile successfully")
    void testGetUserProfileHandler_Success() throws UserException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);

        // Act
        ResponseEntity<UserDto> response = userController.getUserProfileHandler(jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUserId, response.getBody().getId());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
    }

    @Test
    @DisplayName("Should throw UserException when JWT is invalid")
    void testGetUserProfileHandler_InvalidJwt() throws UserException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Invalid JWT"));

        // Act & Assert
        assertThrows(UserException.class, () -> userController.getUserProfileHandler(jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void testGetUserByIdHandler_Success() throws UserException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(userService.findUserById(testUserId)).thenReturn(testUser);

        // Act
        ResponseEntity<UserDto> response = userController.getUserByIdHandler(testUserId, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(userService, times(1)).findUserById(testUserId);
    }

    @Test
    @DisplayName("Should throw UserException when user not found by ID")
    void testGetUserByIdHandler_UserNotFound() throws UserException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(userService.findUserById(anyLong())).thenThrow(new UserException("User not found"));

        // Act & Assert
        assertThrows(UserException.class, () -> userController.getUserByIdHandler(999L, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
    }

    @Test
    @DisplayName("Should search users successfully")
    void testSearchUserHandler_Success() throws UserException {
        // Arrange
        List<User> searchResults = new ArrayList<>();
        searchResults.add(testUser);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(userService.searchUser("Test")).thenReturn(searchResults);

        // Act
        ResponseEntity<List<UserDto>> response = userController.searchUserHandler("Test", jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(userService, times(1)).searchUser("Test");
    }

    @Test
    @DisplayName("Should return empty list when search yields no results")
    void testSearchUserHandler_NoResults() throws UserException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(userService.searchUser("NonExistent")).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<UserDto>> response = userController.searchUserHandler("NonExistent", jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(userService, times(1)).searchUser("NonExistent");
    }

    @Test
    @DisplayName("Should update user successfully")
    void testUpdateUserHandler_Success() throws UserException {
        // Arrange
        User updateRequest = new User();
        updateRequest.setFullName("Updated Name");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(userService.updateUser(testUserId, updateRequest)).thenReturn(testUser);

        // Act
        ResponseEntity<UserDto> response = userController.updateUserHandler(updateRequest, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(userService, times(1)).updateUser(testUserId, updateRequest);
    }

    @Test
    @DisplayName("Should throw UserException when updating non-existent user")
    void testUpdateUserHandler_UserNotFound() throws UserException {
        // Arrange
        User updateRequest = new User();
        updateRequest.setFullName("Updated Name");

        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("User not found"));

        // Act & Assert
        assertThrows(UserException.class, () -> userController.updateUserHandler(updateRequest, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
    }
}
