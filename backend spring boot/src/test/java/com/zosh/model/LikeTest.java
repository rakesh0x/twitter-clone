package com.zosh.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Like Model Tests")
class LikeTest {

    private Like like;
    private User testUser;
    private Twit testTwit;

    @BeforeEach
    void setUp() {
        like = new Like();
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");

        testTwit = new Twit();
        testTwit.setId(1L);
        testTwit.setContent("Test Tweet");
    }

    @Test
    @DisplayName("Should create like with default values")
    void testLikeCreation_Default() {
        // Assert
        assertNull(like.getId());
        assertNull(like.getUser());
        assertNull(like.getTwit());
    }

    @Test
    @DisplayName("Should set and get like ID")
    void testSetGetId() {
        // Arrange & Act
        like.setId(1L);

        // Assert
        assertEquals(1L, like.getId());
    }

    @Test
    @DisplayName("Should set and get user")
    void testSetGetUser() {
        // Arrange & Act
        like.setUser(testUser);

        // Assert
        assertEquals(testUser, like.getUser());
        assertEquals("testuser@example.com", like.getUser().getEmail());
    }

    @Test
    @DisplayName("Should set and get twit")
    void testSetGetTwit() {
        // Arrange & Act
        like.setTwit(testTwit);

        // Assert
        assertEquals(testTwit, like.getTwit());
        assertEquals("Test Tweet", like.getTwit().getContent());
    }

    @Test
    @DisplayName("Should set user and twit together")
    void testSetUserAndTwit() {
        // Arrange & Act
        like.setUser(testUser);
        like.setTwit(testTwit);

        // Assert
        assertNotNull(like.getUser());
        assertNotNull(like.getTwit());
        assertEquals(testUser.getId(), like.getUser().getId());
        assertEquals(testTwit.getId(), like.getTwit().getId());
    }

    @Test
    @DisplayName("Should handle multiple likes for same twit by different users")
    void testMultipleLikesForSameTwit() {
        // Arrange
        Like like1 = new Like();
        like1.setId(1L);
        User user1 = new User();
        user1.setId(1L);
        like1.setUser(user1);
        like1.setTwit(testTwit);

        Like like2 = new Like();
        like2.setId(2L);
        User user2 = new User();
        user2.setId(2L);
        like2.setUser(user2);
        like2.setTwit(testTwit);

        // Assert
        assertEquals(testTwit, like1.getTwit());
        assertEquals(testTwit, like2.getTwit());
        assertNotEquals(like1.getUser().getId(), like2.getUser().getId());
    }

    @Test
    @DisplayName("Should handle same user liking multiple twits")
    void testSameUserLikingMultipleTwits() {
        // Arrange
        Like like1 = new Like();
        like1.setId(1L);
        like1.setUser(testUser);
        Twit twit1 = new Twit();
        twit1.setId(1L);
        like1.setTwit(twit1);

        Like like2 = new Like();
        like2.setId(2L);
        like2.setUser(testUser);
        Twit twit2 = new Twit();
        twit2.setId(2L);
        like2.setTwit(twit2);

        // Assert
        assertEquals(testUser, like1.getUser());
        assertEquals(testUser, like2.getUser());
        assertNotEquals(like1.getTwit().getId(), like2.getTwit().getId());
    }
}
