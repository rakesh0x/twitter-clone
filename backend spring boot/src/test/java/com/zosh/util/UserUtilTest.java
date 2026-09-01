package com.zosh.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.zosh.model.User;
import com.zosh.model.Varification;

@DisplayName("UserUtil Tests")
class UserUtilTest {

    private User reqUser;
    private User targetUser;

    @BeforeEach
    void setUp() {
        reqUser = new User();
        reqUser.setId(1L);
        reqUser.setFollowings(new ArrayList<>());

        targetUser = new User();
        targetUser.setId(2L);
    }

    @Test
    @DisplayName("Should return true when users have same ID")
    void testIsReqUser_True() {
        // Arrange
        User sameUser = new User();
        sameUser.setId(1L);

        // Act
        boolean result = UserUtil.isReqUser(reqUser, sameUser);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when users have different IDs")
    void testIsReqUser_False() {
        // Act
        boolean result = UserUtil.isReqUser(reqUser, targetUser);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when targetUser is followed by reqUser")
    void testIsFollowedByReqUser_True() {
        // Arrange
        reqUser.getFollowings().add(targetUser);

        // Act
        boolean result = UserUtil.isFollowedByReqUser(reqUser, targetUser);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when targetUser is not followed by reqUser")
    void testIsFollowedByReqUser_False() {
        // Act
        boolean result = UserUtil.isFollowedByReqUser(reqUser, targetUser);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return true when verification end date is in the future")
    void testIsVerified_True() {
        // Arrange
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        // Act
        boolean result = UserUtil.isVerified(futureDate);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false when verification end date is in the past")
    void testIsVerified_False() {
        // Arrange
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

        // Act
        boolean result = UserUtil.isVerified(pastDate);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when verification end date is null")
    void testIsVerified_Null() {
        // Act
        boolean result = UserUtil.isVerified(null);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when verification end date is exactly now")
    void testIsVerified_Now() {
        // Arrange
        LocalDateTime nowDate = LocalDateTime.now();

        // Act
        boolean result = UserUtil.isVerified(nowDate);

        // Assert
        // isAfter() returns false when dates are equal
        assertFalse(result);
    }
}
