package com.zosh.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.zosh.model.Like;
import com.zosh.model.Twit;
import com.zosh.model.User;

@DisplayName("TweetUtil Tests")
class TweetUtilTest {

    private User reqUser;
    private Twit testTwit;

    @BeforeEach
    void setUp() {
        reqUser = new User();
        reqUser.setId(1L);

        testTwit = new Twit();
        testTwit.setId(1L);
        testTwit.setContent("Test Tweet");
        testTwit.setCreatedAt(LocalDateTime.now());
        testTwit.setLikes(new ArrayList<>());
        testTwit.setRetwitUser(new ArrayList<>());
    }

    @Test
    @DisplayName("Should return true when twit is liked by requesting user")
    void testIsLikedByReqUser_True() {
        // Arrange
        Like like = new Like();
        like.setUser(reqUser);
        like.setTwit(testTwit);
        testTwit.getLikes().add(like);

        // Act
        boolean result = TweetUtil.isLikedByReqUser(reqUser, testTwit);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when twit is not liked by requesting user")
    void testIsLikedByReqUser_False() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);

        Like like = new Like();
        like.setUser(otherUser);
        like.setTwit(testTwit);
        testTwit.getLikes().add(like);

        // Act
        boolean result = TweetUtil.isLikedByReqUser(reqUser, testTwit);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when twit has no likes")
    void testIsLikedByReqUser_NoLikes() {
        // Act
        boolean result = TweetUtil.isLikedByReqUser(reqUser, testTwit);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when likes list is null")
    void testIsLikedByReqUser_NullLikes() {
        // Arrange
        testTwit.setLikes(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> TweetUtil.isLikedByReqUser(reqUser, testTwit));
    }

    @Test
    @DisplayName("Should return true when twit is retwitted by requesting user")
    void testIsRetwitedByReqUser_True() {
        // Arrange
        testTwit.getRetwitUser().add(reqUser);

        // Act
        boolean result = TweetUtil.isRetwitedByReqUser(reqUser, testTwit);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when twit is not retwitted by requesting user")
    void testIsRetwitedByReqUser_False() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        testTwit.getRetwitUser().add(otherUser);

        // Act
        boolean result = TweetUtil.isRetwitedByReqUser(reqUser, testTwit);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when twit has no retwits")
    void testIsRetwitedByReqUser_NoRetwits() {
        // Act
        boolean result = TweetUtil.isRetwitedByReqUser(reqUser, testTwit);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when retwit users list is null")
    void testIsRetwitedByReqUser_NullRetwitUsers() {
        // Arrange
        testTwit.setRetwitUser(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> TweetUtil.isRetwitedByReqUser(reqUser, testTwit));
    }

    @Test
    @DisplayName("Should return true when multiple users retwiteed and one is requesting user")
    void testIsRetwitedByReqUser_MultipleRetwits() {
        // Arrange
        User otherUser1 = new User();
        otherUser1.setId(2L);
        User otherUser2 = new User();
        otherUser2.setId(3L);

        testTwit.getRetwitUser().add(otherUser1);
        testTwit.getRetwitUser().add(reqUser);
        testTwit.getRetwitUser().add(otherUser2);

        // Act
        boolean result = TweetUtil.isRetwitedByReqUser(reqUser, testTwit);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return true when multiple users liked and one is requesting user")
    void testIsLikedByReqUser_MultipleLikes() {
        // Arrange
        User otherUser1 = new User();
        otherUser1.setId(2L);
        User otherUser2 = new User();
        otherUser2.setId(3L);

        Like like1 = new Like();
        like1.setUser(otherUser1);
        testTwit.getLikes().add(like1);

        Like like2 = new Like();
        like2.setUser(reqUser);
        testTwit.getLikes().add(like2);

        Like like3 = new Like();
        like3.setUser(otherUser2);
        testTwit.getLikes().add(like3);

        // Act
        boolean result = TweetUtil.isLikedByReqUser(reqUser, testTwit);

        // Assert
        assertTrue(result);
    }
}
