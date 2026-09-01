package com.zosh.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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

import com.zosh.dto.TwitDto;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.request.TwitReplyRequest;
import com.zosh.response.ApiResponse;
import com.zosh.service.TwitService;
import com.zosh.service.UserService;

@ExtendWith(MockitoExtension.class)
@DisplayName("TwitController Tests")
class TwitControllerTest {

    @Mock
    private TwitService twitService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TwitController twitController;

    private Twit testTwit;
    private User testUser;
    private Long twitId;
    private Long userId;
    private String jwt;

    @BeforeEach
    void setUp() {
        twitId = 1L;
        userId = 1L;
        jwt = "Bearer valid.jwt.token";

        testUser = new User();
        testUser.setId(userId);
        testUser.setFullName("Test User");
        testUser.setEmail("testuser@example.com");

        testTwit = new Twit();
        testTwit.setId(twitId);
        testTwit.setUser(testUser);
        testTwit.setContent("Test Tweet Content");
        testTwit.setCreatedAt(LocalDateTime.now());
        testTwit.setTwit(true);
        testTwit.setReply(false);
        testTwit.setLikes(new ArrayList<>());
        testTwit.setReplyTwits(new ArrayList<>());
        testTwit.setRetwitUser(new ArrayList<>());
    }

    @Test
    @DisplayName("Should create twit successfully")
    void testCreateTwit_Success() throws UserException, TwitException {
        // Arrange
        Twit twitRequest = new Twit();
        twitRequest.setContent("New Tweet");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(twitService.createTwit(twitRequest, testUser)).thenReturn(testTwit);

        // Act
        ResponseEntity<TwitDto> response = twitController.createTwit(twitRequest, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(twitService, times(1)).createTwit(twitRequest, testUser);
    }

    @Test
    @DisplayName("Should throw UserException when creating twit with invalid JWT")
    void testCreateTwit_InvalidJwt() throws UserException {
        // Arrange
        Twit twitRequest = new Twit();
        twitRequest.setContent("New Tweet");

        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Invalid JWT"));

        // Act & Assert
        assertThrows(UserException.class, () -> twitController.createTwit(twitRequest, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(twitService, never()).createTwit(any(), any());
    }

    @Test
    @DisplayName("Should reply to twit successfully")
    void testReplyTwit_Success() throws UserException, TwitException {
        // Arrange
        TwitReplyRequest replyRequest = new TwitReplyRequest();
        replyRequest.setTwitId(twitId);
        replyRequest.setContent("Reply Content");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(twitService.createReply(replyRequest, testUser)).thenReturn(testTwit);

        // Act
        ResponseEntity<TwitDto> response = twitController.replyTwit(replyRequest, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(twitService, times(1)).createReply(replyRequest, testUser);
    }

    @Test
    @DisplayName("Should retwit successfully")
    void testRetwit_Success() throws UserException, TwitException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(twitService.retwit(twitId, testUser)).thenReturn(testTwit);

        // Act
        ResponseEntity<TwitDto> response = twitController.retwit(twitId, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(twitService, times(1)).retwit(twitId, testUser);
    }

    @Test
    @DisplayName("Should find twit by ID successfully")
    void testFindTwitById_Success() throws TwitException, UserException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(twitService.findById(twitId)).thenReturn(testTwit);

        // Act
        ResponseEntity<TwitDto> response = twitController.findTwitById(twitId, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(twitService, times(1)).findById(twitId);
    }

    @Test
    @DisplayName("Should throw TwitException when twit not found")
    void testFindTwitById_NotFound() throws UserException, TwitException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(twitService.findById(anyLong())).thenThrow(new TwitException("Twit not found"));

        // Act & Assert
        assertThrows(TwitException.class, () -> twitController.findTwitById(twitId, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(twitService, times(1)).findById(twitId);
    }

    @Test
    @DisplayName("Should delete twit successfully")
    void testDeleteTwitById_Success() throws UserException, TwitException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        doNothing().when(twitService).deleteTwitById(twitId, userId);

        // Act
        ResponseEntity<ApiResponse> response = twitController.deleteTwitById(twitId, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isStatus());
        assertEquals("twit deleted successfully", response.getBody().getMessage());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(twitService, times(1)).deleteTwitById(twitId, userId);
    }

    @Test
    @DisplayName("Should throw UserException when deleting twit with invalid JWT")
    void testDeleteTwitById_InvalidJwt() throws UserException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Invalid JWT"));

        // Act & Assert
        assertThrows(UserException.class, () -> twitController.deleteTwitById(twitId, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(twitService, never()).deleteTwitById(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Should find all twits successfully")
    void testFindAllTwits_Success() throws UserException {
        // Arrange
        List<Twit> twits = new ArrayList<>();
        twits.add(testTwit);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(twitService.findAllTwit()).thenReturn(twits);

        // Act
        ResponseEntity<List<TwitDto>> response = twitController.findAllTwits(jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(twitService, times(1)).findAllTwit();
    }

    @Test
    @DisplayName("Should return empty list when no twits exist")
    void testFindAllTwits_Empty() throws UserException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(twitService.findAllTwit()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<TwitDto>> response = twitController.findAllTwits(jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(twitService, times(1)).findAllTwit();
    }

    @Test
    @DisplayName("Should get user's twits successfully")
    void testGetUsersTwits_Success() throws UserException {
        // Arrange
        List<Twit> userTwits = new ArrayList<>();
        userTwits.add(testTwit);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(userService.findUserById(userId)).thenReturn(testUser);
        when(twitService.getUsersTwit(testUser)).thenReturn(userTwits);

        // Act
        ResponseEntity<List<TwitDto>> response = twitController.getUsersTwits(userId, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(userService, times(1)).findUserById(userId);
        verify(twitService, times(1)).getUsersTwit(testUser);
    }

    @Test
    @DisplayName("Should find twits liked by user successfully")
    void testFindTwitByLikesContainsUser_Success() throws UserException {
        // Arrange
        List<Twit> likedTwits = new ArrayList<>();
        likedTwits.add(testTwit);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(userService.findUserById(userId)).thenReturn(testUser);
        when(twitService.findByLikesContainsUser(testUser)).thenReturn(likedTwits);

        // Act
        ResponseEntity<List<TwitDto>> response = twitController.findTwitByLikesContainsUser(userId, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(userService, times(1)).findUserById(userId);
        verify(twitService, times(1)).findByLikesContainsUser(testUser);
    }
}
