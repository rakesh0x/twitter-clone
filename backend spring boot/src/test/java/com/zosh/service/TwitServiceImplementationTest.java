package com.zosh.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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

import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.repository.TwitRepository;
import com.zosh.request.TwitReplyRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("TwitServiceImplementation Tests")
class TwitServiceImplementationTest {

    @Mock
    private TwitRepository twitRepository;

    @InjectMocks
    private TwitServiceImplementation twitService;

    private Twit testTwit;
    private User testUser;
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
    void testCreateTwit_Success() {
        // Arrange
        Twit twitRequest = new Twit();
        twitRequest.setContent("New Tweet");
        twitRequest.setImage("https://example.com/image.jpg");
        twitRequest.setVideo(null);

        when(twitRepository.save(any(Twit.class))).thenReturn(testTwit);

        // Act
        Twit result = twitService.createTwit(twitRequest, testUser);

        // Assert
        assertNotNull(result);
        assertEquals("Test Tweet Content", result.getContent());
        assertEquals(testUser, result.getUser());
        assertTrue(result.isTwit());
        assertFalse(result.isReply());
        assertNotNull(result.getCreatedAt());
        verify(twitRepository, times(1)).save(any(Twit.class));
    }

    @Test
    @DisplayName("Should create twit with video successfully")
    void testCreateTwit_WithVideo() {
        // Arrange
        Twit twitRequest = new Twit();
        twitRequest.setContent("Tweet with Video");
        twitRequest.setVideo("https://example.com/video.mp4");

        when(twitRepository.save(any(Twit.class))).thenReturn(testTwit);

        // Act
        Twit result = twitService.createTwit(twitRequest, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        verify(twitRepository, times(1)).save(any(Twit.class));
    }

    @Test
    @DisplayName("Should find all twits successfully")
    void testFindAllTwit_Success() {
        // Arrange
        List<Twit> twits = new ArrayList<>();
        twits.add(testTwit);

        Twit twit2 = new Twit();
        twit2.setId(2L);
        twit2.setContent("Second Tweet");
        twit2.setTwit(true);
        twit2.setReply(false);
        twits.add(twit2);

        when(twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc()).thenReturn(twits);

        // Act
        List<Twit> results = twitService.findAllTwit();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(twitRepository, times(1)).findAllByIsTwitTrueOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("Should return empty list when no twits exist")
    void testFindAllTwit_Empty() {
        // Arrange
        when(twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc()).thenReturn(new ArrayList<>());

        // Act
        List<Twit> results = twitService.findAllTwit();

        // Assert
        assertNotNull(results);
        assertEquals(0, results.size());
        verify(twitRepository, times(1)).findAllByIsTwitTrueOrderByCreatedAtDesc();
    }

    @Test
    @DisplayName("Should retwit successfully when user hasn't already retwitted")
    void testRetwit_Success() throws TwitException {
        // Arrange
        User retwitUser = new User();
        retwitUser.setId(2L);

        when(twitRepository.findById(twitId)).thenReturn(Optional.of(testTwit));
        when(twitRepository.save(any(Twit.class))).thenReturn(testTwit);

        // Act
        Twit result = twitService.retwit(twitId, retwitUser);

        // Assert
        assertNotNull(result);
        assertTrue(result.getRetwitUser().contains(retwitUser));
        verify(twitRepository, times(1)).findById(twitId);
        verify(twitRepository, times(1)).save(testTwit);
    }

    @Test
    @DisplayName("Should remove retwit when user has already retwitted")
    void testRetwit_Remove() throws TwitException {
        // Arrange
        User retwitUser = new User();
        retwitUser.setId(2L);
        testTwit.getRetwitUser().add(retwitUser);

        when(twitRepository.findById(twitId)).thenReturn(Optional.of(testTwit));
        when(twitRepository.save(any(Twit.class))).thenReturn(testTwit);

        // Act
        Twit result = twitService.retwit(twitId, retwitUser);

        // Assert
        assertNotNull(result);
        assertFalse(result.getRetwitUser().contains(retwitUser));
        verify(twitRepository, times(1)).findById(twitId);
        verify(twitRepository, times(1)).save(testTwit);
    }

    @Test
    @DisplayName("Should find twit by ID successfully")
    void testFindById_Success() throws TwitException {
        // Arrange
        when(twitRepository.findById(twitId)).thenReturn(Optional.of(testTwit));

        // Act
        Twit result = twitService.findById(twitId);

        // Assert
        assertNotNull(result);
        assertEquals(twitId, result.getId());
        assertEquals("Test Tweet Content", result.getContent());
        verify(twitRepository, times(1)).findById(twitId);
    }

    @Test
    @DisplayName("Should throw TwitException when twit not found by ID")
    void testFindById_NotFound() {
        // Arrange
        when(twitRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TwitException.class, () -> twitService.findById(twitId));
        verify(twitRepository, times(1)).findById(twitId);
    }

    @Test
    @DisplayName("Should delete twit successfully by authorized user")
    void testDeleteTwitById_Success() throws TwitException, UserException {
        // Arrange
        when(twitRepository.findById(twitId)).thenReturn(Optional.of(testTwit));
        doNothing().when(twitRepository).deleteById(twitId);

        // Act
        twitService.deleteTwitById(twitId, userId);

        // Assert
        verify(twitRepository, times(1)).findById(twitId);
        verify(twitRepository, times(1)).deleteById(twitId);
    }

    @Test
    @DisplayName("Should throw UserException when unauthorized user tries to delete")
    void testDeleteTwitById_Unauthorized() throws TwitException {
        // Arrange
        Long unauthorizedUserId = 999L;
        when(twitRepository.findById(twitId)).thenReturn(Optional.of(testTwit));

        // Act & Assert
        assertThrows(UserException.class, () -> twitService.deleteTwitById(twitId, unauthorizedUserId));
        verify(twitRepository, times(1)).findById(twitId);
        verify(twitRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should throw TwitException when deleting non-existent twit")
    void testDeleteTwitById_TwitNotFound() {
        // Arrange
        when(twitRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TwitException.class, () -> twitService.deleteTwitById(twitId, userId));
        verify(twitRepository, times(1)).findById(twitId);
    }

    @Test
    @DisplayName("Should remove twit from retwit successfully")
    void testRemoveFromRetwit_Success() throws TwitException, UserException {
        // Arrange
        User retwitUser = new User();
        retwitUser.setId(2L);
        testTwit.getRetwitUser().add(retwitUser);

        when(twitRepository.findById(twitId)).thenReturn(Optional.of(testTwit));
        when(twitRepository.save(any(Twit.class))).thenReturn(testTwit);

        // Act
        Twit result = twitService.removeFromRetwit(twitId, retwitUser);

        // Assert
        assertNotNull(result);
        assertFalse(result.getRetwitUser().contains(retwitUser));
        verify(twitRepository, times(1)).findById(twitId);
        verify(twitRepository, times(1)).save(testTwit);
    }

    @Test
    @DisplayName("Should create reply successfully")
    void testCreateReply_Success() throws TwitException {
        // Arrange
        TwitReplyRequest replyRequest = new TwitReplyRequest();
        replyRequest.setTwitId(twitId);
        replyRequest.setContent("Reply Content");
        replyRequest.setImage(null);

        when(twitRepository.findById(twitId)).thenReturn(Optional.of(testTwit));
        when(twitRepository.save(any(Twit.class))).thenReturn(testTwit);

        // Act
        Twit result = twitService.createReply(replyRequest, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(twitId, result.getId());
        verify(twitRepository, times(2)).findById(twitId);
        verify(twitRepository, times(2)).save(any(Twit.class));
    }

    @Test
    @DisplayName("Should throw TwitException when creating reply to non-existent twit")
    void testCreateReply_TwitNotFound() {
        // Arrange
        TwitReplyRequest replyRequest = new TwitReplyRequest();
        replyRequest.setTwitId(999L);
        replyRequest.setContent("Reply Content");

        when(twitRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TwitException.class, () -> twitService.createReply(replyRequest, testUser));
        verify(twitRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should get user's twits successfully")
    void testGetUsersTwit_Success() {
        // Arrange
        List<Twit> userTwits = new ArrayList<>();
        userTwits.add(testTwit);

        when(twitRepository.findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(testUser, userId))
                .thenReturn(userTwits);

        // Act
        List<Twit> results = twitService.getUsersTwit(testUser);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(twitRepository, times(1))
                .findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(testUser, userId);
    }

    @Test
    @DisplayName("Should get twits liked by user successfully")
    void testFindByLikesContainsUser_Success() {
        // Arrange
        List<Twit> likedTwits = new ArrayList<>();
        likedTwits.add(testTwit);

        when(twitRepository.findByLikesUser_Id(userId)).thenReturn(likedTwits);

        // Act
        List<Twit> results = twitService.findByLikesContainsUser(testUser);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(twitRepository, times(1)).findByLikesUser_Id(userId);
    }
}
