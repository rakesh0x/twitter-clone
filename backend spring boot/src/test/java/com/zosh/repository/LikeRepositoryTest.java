package com.zosh.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.zosh.model.Like;
import com.zosh.model.Twit;
import com.zosh.model.User;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("LikeRepository Tests")
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TwitRepository twitRepository;

    private User testUser;
    private User otherUser;
    private Twit testTwit;
    private Like testLike;

    @BeforeEach
    void setUp() {
        testUser.setFullName("Test User");
        testUser.setEmail("testuser@example.com");
        testUser = userRepository.save(testUser);

        otherUser = new User();
        otherUser.setFullName("Other User");
        otherUser.setEmail("otheruser@example.com");
        otherUser = userRepository.save(otherUser);

        testTwit = new Twit();
        testTwit.setUser(testUser);
        testTwit.setContent("Test Tweet");
        testTwit.setCreatedAt(LocalDateTime.now());
        testTwit.setTwit(true);
        testTwit.setReply(false);
        testTwit = twitRepository.save(testTwit);

        testLike = new Like();
        testLike.setUser(testUser);
        testLike.setTwit(testTwit);
        testLike = likeRepository.save(testLike);
    }

    @Test
    @DisplayName("Should check if like exists for user and twit")
    void testIsLikeExist_Success() {
        // Act
        Like result = likeRepository.isLikeExist(testUser.getId(), testTwit.getId());

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals(testTwit.getId(), result.getTwit().getId());
    }

    @Test
    @DisplayName("Should return null when like does not exist")
    void testIsLikeExist_NotFound() {
        // Act
        Like result = likeRepository.isLikeExist(otherUser.getId(), testTwit.getId());

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should find all likes for a twit")
    void testFindByTwitId_Success() {
        // Arrange
        Like like2 = new Like();
        like2.setUser(otherUser);
        like2.setTwit(testTwit);
        likeRepository.save(like2);

        // Act
        List<Like> results = likeRepository.findByTwitId(testTwit.getId());

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(l -> l.getTwit().getId().equals(testTwit.getId())));
    }

    @Test
    @DisplayName("Should return empty list when twit has no likes")
    void testFindByTwitId_Empty() {
        // Arrange
        Twit twitWithoutLikes = new Twit();
        twitWithoutLikes.setUser(testUser);
        twitWithoutLikes.setContent("Tweet without likes");
        twitWithoutLikes.setCreatedAt(LocalDateTime.now());
        twitWithoutLikes.setTwit(true);
        twitWithoutLikes.setReply(false);
        twitWithoutLikes = twitRepository.save(twitWithoutLikes);

        // Act
        List<Like> results = likeRepository.findByTwitId(twitWithoutLikes.getId());

        // Assert
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    @DisplayName("Should find like by ID successfully")
    void testFindById_Success() {
        // Act
        Optional<Like> result = likeRepository.findById(testLike.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testLike.getId(), result.get().getId());
        assertEquals(testUser.getId(), result.get().getUser().getId());
    }

    @Test
    @DisplayName("Should return empty Optional when like not found by ID")
    void testFindById_NotFound() {
        // Act
        Optional<Like> result = likeRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should save like successfully")
    void testSave_Success() {
        // Arrange
        Like newLike = new Like();
        newLike.setUser(otherUser);
        newLike.setTwit(testTwit);

        // Act
        Like savedLike = likeRepository.save(newLike);

        // Assert
        assertNotNull(savedLike.getId());
        assertEquals(otherUser.getId(), savedLike.getUser().getId());
        assertEquals(testTwit.getId(), savedLike.getTwit().getId());
    }

    @Test
    @DisplayName("Should delete like successfully")
    void testDelete_Success() {
        // Arrange
        Long likeId = testLike.getId();

        // Act
        likeRepository.deleteById(likeId);

        // Assert
        assertFalse(likeRepository.findById(likeId).isPresent());
    }

    @Test
    @DisplayName("Should find multiple likes for a twit by multiple users")
    void testFindByTwitId_MultipleLikes() {
        // Arrange
        User user3 = new User();
        user3.setFullName("User 3");
        user3.setEmail("user3@example.com");
        user3 = userRepository.save(user3);
        final Long user3Id = user3.getId();

        Like like3 = new Like();
        like3.setUser(user3);
        like3.setTwit(testTwit);
        likeRepository.save(like3);

        // Act
        List<Like> results = likeRepository.findByTwitId(testTwit.getId());

        // Assert
        assertNotNull(results);
        assertEquals(3, results.size());
        assertTrue(results.stream().map(Like::getUser).map(User::getId)
                .anyMatch(uid -> uid.equals(testUser.getId())));
        assertTrue(results.stream().map(Like::getUser).map(User::getId)
                .anyMatch(uid -> uid.equals(otherUser.getId())));
        assertTrue(results.stream().map(Like::getUser).map(User::getId)
            .anyMatch(uid -> uid.equals(user3Id)));
    }
}
