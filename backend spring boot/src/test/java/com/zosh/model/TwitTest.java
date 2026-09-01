package com.zosh.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Twit Model Tests")
class TwitTest {

    private Twit twit;
    private User testUser;

    @BeforeEach
    void setUp() {
        twit = new Twit();
        testUser = new User();
        testUser.setId(1L);
        testUser.setFullName("Test User");
    }

    @Test
    @DisplayName("Should create twit with default values")
    void testTwitCreation_Default() {
        // Assert
        assertNull(twit.getId());
        assertNull(twit.getContent());
        assertNull(twit.getCreatedAt());
        assertFalse(twit.isReply());
        assertFalse(twit.isTwit());
        assertFalse(twit.is_liked());
            assertFalse(twit.is_retwit());
    }

    @Test
    @DisplayName("Should set and get twit ID")
    void testSetGetId() {
        // Arrange & Act
        twit.setId(1L);

        // Assert
        assertEquals(1L, twit.getId());
    }

    @Test
    @DisplayName("Should set and get user")
    void testSetGetUser() {
        // Arrange & Act
        twit.setUser(testUser);

        // Assert
        assertEquals(testUser, twit.getUser());
        assertEquals("Test User", twit.getUser().getFullName());
    }

    @Test
    @DisplayName("Should set and get content")
    void testSetGetContent() {
        // Arrange & Act
        twit.setContent("This is a test tweet");

        // Assert
        assertEquals("This is a test tweet", twit.getContent());
    }

    @Test
    @DisplayName("Should set and get created at timestamp")
    void testSetGetCreatedAt() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        // Act
        twit.setCreatedAt(now);

        // Assert
        assertEquals(now, twit.getCreatedAt());
        assertNotNull(twit.getCreatedAt());
    }

    @Test
    @DisplayName("Should set and get image")
    void testSetGetImage() {
        // Arrange & Act
        twit.setImage("https://example.com/image.jpg");

        // Assert
        assertEquals("https://example.com/image.jpg", twit.getImage());
    }

    @Test
    @DisplayName("Should set and get video")
    void testSetGetVideo() {
        // Arrange & Act
        twit.setVideo("https://example.com/video.mp4");

        // Assert
        assertEquals("https://example.com/video.mp4", twit.getVideo());
    }

    @Test
    @DisplayName("Should set and get isReply flag")
    void testSetGetIsReply() {
        // Arrange & Act
        twit.setReply(true);

        // Assert
        assertTrue(twit.isReply());
    }

    @Test
    @DisplayName("Should set and get isTwit flag")
    void testSetGetIsTwit() {
        // Arrange & Act
        twit.setTwit(true);

        // Assert
        assertTrue(twit.isTwit());
    }



    @Test
    @DisplayName("Should initialize likes list")
    void testInitializeLikesList() {
        // Arrange & Act
        twit.setLikes(new ArrayList<>());

        // Assert
        assertNotNull(twit.getLikes());
        assertEquals(0, twit.getLikes().size());
    }

    @Test
    @DisplayName("Should add like to twit")
    void testAddLike() {
        // Arrange
        twit.setLikes(new ArrayList<>());
        Like like = new Like();
        like.setUser(testUser);
        like.setTwit(twit);

        // Act
        twit.getLikes().add(like);

        // Assert
        assertEquals(1, twit.getLikes().size());
        assertTrue(twit.getLikes().contains(like));
    }

    @Test
    @DisplayName("Should initialize reply twits list")
    void testInitializeReplyTwitsList() {
        // Arrange & Act
        twit.setReplyTwits(new ArrayList<>());

        // Assert
        assertNotNull(twit.getReplyTwits());
        assertEquals(0, twit.getReplyTwits().size());
    }

    @Test
    @DisplayName("Should add reply twit")
    void testAddReplyTwit() {
        // Arrange
        twit.setReplyTwits(new ArrayList<>());
        Twit replyTwit = new Twit();
        replyTwit.setContent("This is a reply");
        replyTwit.setReply(true);

        // Act
        twit.getReplyTwits().add(replyTwit);

        // Assert
        assertEquals(1, twit.getReplyTwits().size());
        assertTrue(twit.getReplyTwits().contains(replyTwit));
    }

    @Test
    @DisplayName("Should initialize retwit users list")
    void testInitializeRetwitUsersList() {
        // Arrange & Act
        twit.setRetwitUser(new ArrayList<>());

        // Assert
        assertNotNull(twit.getRetwitUser());
        assertEquals(0, twit.getRetwitUser().size());
    }

    @Test
    @DisplayName("Should add user to retwit list")
    void testAddRetwitUser() {
        // Arrange
        twit.setRetwitUser(new ArrayList<>());
        User retwitUser = new User();
        retwitUser.setId(2L);

        // Act
        twit.getRetwitUser().add(retwitUser);

        // Assert
        assertEquals(1, twit.getRetwitUser().size());
        assertTrue(twit.getRetwitUser().contains(retwitUser));
    }

    @Test
    @DisplayName("Should set and get replyFor")
    void testSetGetReplyFor() {
        // Arrange
        Twit originalTwit = new Twit();
        originalTwit.setId(1L);
        originalTwit.setContent("Original tweet");

        // Act
        twit.setReplyFor(originalTwit);

        // Assert
        assertEquals(originalTwit, twit.getReplyFor());
        assertEquals("Original tweet", twit.getReplyFor().getContent());
    }

    @Test
    @DisplayName("Should differentiate between twit and reply")
    void testTwitVsReply() {
        // Arrange & Act
        Twit mainTwit = new Twit();
        mainTwit.setTwit(true);
        mainTwit.setReply(false);

        Twit replyTwit = new Twit();
        replyTwit.setTwit(false);
        replyTwit.setReply(true);

        // Assert
        assertTrue(mainTwit.isTwit());
        assertFalse(mainTwit.isReply());
        
        assertFalse(replyTwit.isTwit());
        assertTrue(replyTwit.isReply());
    }
}
