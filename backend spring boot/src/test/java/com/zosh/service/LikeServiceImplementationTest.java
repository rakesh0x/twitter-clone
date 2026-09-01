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

import com.zosh.exception.LikeException;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.Like;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.repository.LikeRepository;
import com.zosh.repository.TwitRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("LikeServiceImplementation Tests")
class LikeServiceImplementationTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private TwitService twitService;

    @Mock
    private TwitRepository twitRepository;

    @InjectMocks
    private LikeServiceImplementation likeService;

    private Like testLike;
    private User testUser;
    private Twit testTwit;
    private Long twitId;
    private Long userId;

    @BeforeEach
    void setUp() {
        twitId = 1L;
        userId = 1L;

        testUser = new User();
        testUser.setId(userId);
        testUser.setFullName("Test User");
        testUser.setEmail("testuser@example.com");

        testTwit = new Twit();
        testTwit.setId(twitId);
        testTwit.setUser(testUser);
        testTwit.setContent("Test Tweet");
        testTwit.setLikes(new ArrayList<>());

        testLike = new Like();
        testLike.setId(1L);
        testLike.setUser(testUser);
        testLike.setTwit(testTwit);
    }

    @Test
    @DisplayName("Should like twit successfully when not already liked")
    void testLikeTwit_Success() throws UserException, TwitException {
        // Arrange
        when(likeRepository.isLikeExist(userId, twitId)).thenReturn(null);
        when(twitService.findById(twitId)).thenReturn(testTwit);
        when(likeRepository.save(any(Like.class))).thenReturn(testLike);
        when(twitRepository.save(any(Twit.class))).thenReturn(testTwit);

        // Act
        Like result = likeService.likeTwit(twitId, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(testTwit, result.getTwit());
        verify(likeRepository, times(1)).isLikeExist(userId, twitId);
        verify(twitService, times(1)).findById(twitId);
        verify(likeRepository, times(1)).save(any(Like.class));
        verify(twitRepository, times(1)).save(testTwit);
    }

    @Test
    @DisplayName("Should unlike twit when already liked")
    void testLikeTwit_Unlike() throws UserException, TwitException {
        // Arrange
        when(likeRepository.isLikeExist(userId, twitId)).thenReturn(testLike);
        doNothing().when(likeRepository).deleteById(testLike.getId());

        // Act
        Like result = likeService.likeTwit(twitId, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(testLike.getId(), result.getId());
        verify(likeRepository, times(1)).isLikeExist(userId, twitId);
        verify(likeRepository, times(1)).deleteById(testLike.getId());
        verify(twitService, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw UserException when unliking twit with wrong user")
    void testUnlikeTwit_WrongUser() throws TwitException {
        // Arrange
        Long differentUserId = 999L;
        User differentUser = new User();
        differentUser.setId(differentUserId);
        
        testLike.setUser(differentUser);

        when(likeRepository.findById(twitId)).thenReturn(Optional.of(testLike));

        // Act & Assert
        assertThrows(UserException.class, () -> likeService.unlikeTwit(twitId, testUser));
        verify(likeRepository, times(1)).findById(twitId);
        verify(likeRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should throw LikeException when unlike non-existent like")
    void testUnlikeTwit_NotFound() {
        // Arrange
        when(likeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LikeException.class, () -> likeService.unlikeTwit(twitId, testUser));
        verify(likeRepository, times(1)).findById(twitId);
        verify(likeRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should get all likes for a twit successfully")
    void testGetAllLikes_Success() throws TwitException {
        // Arrange
        List<Like> likes = new ArrayList<>();
        
        Like like1 = new Like();
        like1.setId(1L);
        like1.setUser(testUser);
        like1.setTwit(testTwit);
        likes.add(like1);

        User user2 = new User();
        user2.setId(2L);
        Like like2 = new Like();
        like2.setId(2L);
        like2.setUser(user2);
        like2.setTwit(testTwit);
        likes.add(like2);

        when(twitService.findById(twitId)).thenReturn(testTwit);
        when(likeRepository.findByTwitId(twitId)).thenReturn(likes);

        // Act
        List<Like> results = likeService.getAllLikes(twitId);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(twitService, times(1)).findById(twitId);
        verify(likeRepository, times(1)).findByTwitId(twitId);
    }

    @Test
    @DisplayName("Should return empty list when twit has no likes")
    void testGetAllLikes_Empty() throws TwitException {
        // Arrange
        when(twitService.findById(twitId)).thenReturn(testTwit);
        when(likeRepository.findByTwitId(twitId)).thenReturn(new ArrayList<>());

        // Act
        List<Like> results = likeService.getAllLikes(twitId);

        // Assert
        assertNotNull(results);
        assertEquals(0, results.size());
        verify(twitService, times(1)).findById(twitId);
        verify(likeRepository, times(1)).findByTwitId(twitId);
    }

    @Test
    @DisplayName("Should throw TwitException when getting likes for non-existent twit")
    void testGetAllLikes_TwitNotFound() throws TwitException {
        // Arrange
        when(twitService.findById(anyLong())).thenThrow(new TwitException("Twit not found"));

        // Act & Assert
        assertThrows(TwitException.class, () -> likeService.getAllLikes(twitId));
        verify(twitService, times(1)).findById(twitId);
        verify(likeRepository, never()).findByTwitId(anyLong());
    }
}
