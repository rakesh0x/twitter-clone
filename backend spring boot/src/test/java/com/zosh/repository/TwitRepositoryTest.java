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

import com.zosh.model.Twit;
import com.zosh.model.User;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("TwitRepository Tests")
class TwitRepositoryTest {

    @Autowired
    private TwitRepository twitRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Twit testTwit;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFullName("Test User");
        testUser.setEmail("testuser@example.com");
        testUser = userRepository.save(testUser);

        testTwit = new Twit();
        testTwit.setUser(testUser);
        testTwit.setContent("Test Tweet");
        testTwit.setCreatedAt(LocalDateTime.now());
        testTwit.setTwit(true);
        testTwit.setReply(false);
        testTwit = twitRepository.save(testTwit);
    }

    @Test
    @DisplayName("Should find all twits by isTwit flag")
    void testFindAllByIsTwitTrueOrderByCreatedAtDesc() {
        // Arrange
        Twit twit2 = new Twit();
        twit2.setUser(testUser);
        twit2.setContent("Second Tweet");
        twit2.setCreatedAt(LocalDateTime.now().plusHours(1));
        twit2.setTwit(true);
        twit2.setReply(false);
        twitRepository.save(twit2);

        // Act
        List<Twit> results = twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc();

        // Assert
        assertNotNull(results);
        assertTrue(results.size() >= 2);
        assertTrue(results.get(0).isTwit());
    }

    @Test
    @DisplayName("Should not return replies when finding all twits")
    void testFindAllByIsTwitTrue_ExcludesReplies() {
        // Arrange
        Twit reply = new Twit();
        reply.setUser(testUser);
        reply.setContent("Reply Tweet");
        reply.setCreatedAt(LocalDateTime.now().plusHours(1));
        reply.setTwit(false);
        reply.setReply(true);
        twitRepository.save(reply);

        // Act
        List<Twit> results = twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc();

        // Assert
        assertNotNull(results);
        assertTrue(results.stream().allMatch(Twit::isTwit));
    }

    @Test
    @DisplayName("Should find twits ordered by created date descending")
    void testFindAllByIsTwitTrueOrderByCreatedAtDesc_OrderVerification() {
        // Arrange
        Twit olderTwit = new Twit();
        olderTwit.setUser(testUser);
        olderTwit.setContent("Older Tweet");
        olderTwit.setCreatedAt(LocalDateTime.now().minusHours(2));
        olderTwit.setTwit(true);
        olderTwit.setReply(false);
        twitRepository.save(olderTwit);

        Twit newerTwit = new Twit();
        newerTwit.setUser(testUser);
        newerTwit.setContent("Newer Tweet");
        newerTwit.setCreatedAt(LocalDateTime.now().plusHours(1));
        newerTwit.setTwit(true);
        newerTwit.setReply(false);
        twitRepository.save(newerTwit);

        // Act
        List<Twit> results = twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc();

        // Assert
        assertNotNull(results);
        if (results.size() > 1) {
            assertTrue(results.get(0).getCreatedAt().isAfter(results.get(results.size() - 1).getCreatedAt()));
        }
    }

    @Test
    @DisplayName("Should return empty list when no twits exist with isTwit flag")
    void testFindAllByIsTwitTrue_Empty() {
        // Arrange
        twitRepository.deleteAll();

        // Act
        List<Twit> results = twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc();

        // Assert
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    @DisplayName("Should find twit by ID successfully")
    void testFindById_Success() {
        // Act
        Optional<Twit> result = twitRepository.findById(testTwit.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testTwit.getId(), result.get().getId());
        assertEquals("Test Tweet", result.get().getContent());
    }

    @Test
    @DisplayName("Should return empty Optional when twit not found by ID")
    void testFindById_NotFound() {
        // Act
        Optional<Twit> result = twitRepository.findById(999L);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should save twit successfully")
    void testSave_Success() {
        // Arrange
        Twit newTwit = new Twit();
        newTwit.setUser(testUser);
        newTwit.setContent("New Tweet to Save");
        newTwit.setCreatedAt(LocalDateTime.now());
        newTwit.setTwit(true);
        newTwit.setReply(false);

        // Act
        Twit savedTwit = twitRepository.save(newTwit);

        // Assert
        assertNotNull(savedTwit.getId());
        assertEquals("New Tweet to Save", savedTwit.getContent());
    }

    @Test
    @DisplayName("Should delete twit successfully")
    void testDelete_Success() {
        // Arrange
        Long twitId = testTwit.getId();

        // Act
        twitRepository.deleteById(twitId);

        // Assert
        assertFalse(twitRepository.findById(twitId).isPresent());
    }
}
