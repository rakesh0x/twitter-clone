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

import com.zosh.dto.LikeDto;
import com.zosh.exception.LikeException;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.Like;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.service.LikesService;
import com.zosh.service.UserService;

@ExtendWith(MockitoExtension.class)
@DisplayName("LikeController Tests")
class LikeControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private LikesService likeService;

    @InjectMocks
    private LikeController likeController;

    private Like testLike;
    private User testUser;
    private Twit testTwit;
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
        testTwit.setContent("Test Tweet");
        testTwit.setLikes(new ArrayList<>());

        testLike = new Like();
        testLike.setId(1L);
        testLike.setUser(testUser);
        testLike.setTwit(testTwit);
    }

    @Test
    @DisplayName("Should like twit successfully")
    void testLikeTwit_Success() throws UserException, TwitException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(likeService.likeTwit(twitId, testUser)).thenReturn(testLike);

        // Act
        ResponseEntity<LikeDto> response = likeController.likeTwit(twitId, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(likeService, times(1)).likeTwit(twitId, testUser);
    }

    @Test
    @DisplayName("Should throw UserException when liking with invalid JWT")
    void testLikeTwit_InvalidJwt() throws UserException, TwitException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Invalid JWT"));

        // Act & Assert
        assertThrows(UserException.class, () -> likeController.likeTwit(twitId, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(likeService, never()).likeTwit(anyLong(), any());
    }

    @Test
    @DisplayName("Should throw TwitException when liking non-existent twit")
    void testLikeTwit_TwitNotFound() throws UserException, TwitException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(likeService.likeTwit(anyLong(), any())).thenThrow(new TwitException("Twit not found"));

        // Act & Assert
        assertThrows(TwitException.class, () -> likeController.likeTwit(twitId, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(likeService, times(1)).likeTwit(twitId, testUser);
    }

    @Test
    @DisplayName("Should unlike twit successfully")
    void testUnlikeTwit_Success() throws UserException, TwitException, LikeException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(likeService.unlikeTwit(twitId, testUser)).thenReturn(testLike);

        // Act
        ResponseEntity<LikeDto> response = likeController.unlikeTwit(twitId, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(likeService, times(1)).unlikeTwit(twitId, testUser);
    }

    @Test
    @DisplayName("Should throw UserException when unliking with invalid JWT")
    void testUnlikeTwit_InvalidJwt() throws UserException, TwitException, LikeException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Invalid JWT"));

        // Act & Assert
        assertThrows(UserException.class, () -> likeController.unlikeTwit(twitId, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(likeService, never()).unlikeTwit(anyLong(), any());
    }

    @Test
    @DisplayName("Should throw LikeException when unliking non-existent like")
    void testUnlikeTwit_LikeNotFound() throws UserException, TwitException, LikeException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(likeService.unlikeTwit(anyLong(), any())).thenThrow(new LikeException("Like not found"));

        // Act & Assert
        assertThrows(LikeException.class, () -> likeController.unlikeTwit(twitId, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(likeService, times(1)).unlikeTwit(twitId, testUser);
    }

    @Test
    @DisplayName("Should get all likes for a twit successfully")
    void testGetAllLike_Success() throws UserException, TwitException {
        // Arrange
        List<Like> likes = new ArrayList<>();
        likes.add(testLike);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(likeService.getAllLikes(twitId)).thenReturn(likes);

        // Act
        ResponseEntity<List<LikeDto>> response = likeController.getAllLike(twitId, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(likeService, times(1)).getAllLikes(twitId);
    }

    @Test
    @DisplayName("Should return empty list when twit has no likes")
    void testGetAllLike_Empty() throws UserException, TwitException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(likeService.getAllLikes(twitId)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<LikeDto>> response = likeController.getAllLike(twitId, jwt);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(likeService, times(1)).getAllLikes(twitId);
    }

    @Test
    @DisplayName("Should throw UserException when getting likes with invalid JWT")
    void testGetAllLike_InvalidJwt() throws UserException, TwitException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Invalid JWT"));

        // Act & Assert
        assertThrows(UserException.class, () -> likeController.getAllLike(twitId, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(likeService, never()).getAllLikes(anyLong());
    }

    @Test
    @DisplayName("Should throw TwitException when twit not found for getting likes")
    void testGetAllLike_TwitNotFound() throws UserException, TwitException {
        // Arrange
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(likeService.getAllLikes(anyLong())).thenThrow(new TwitException("Twit not found"));

        // Act & Assert
        assertThrows(TwitException.class, () -> likeController.getAllLike(twitId, jwt));
        verify(userService, times(1)).findUserProfileByJwt(jwt);
        verify(likeService, times(1)).getAllLikes(twitId);
    }
}
