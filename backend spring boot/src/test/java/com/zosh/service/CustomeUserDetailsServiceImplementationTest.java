package com.zosh.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.zosh.model.User;
import com.zosh.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomeUserDetailsServiceImplementation Tests")
class CustomeUserDetailsServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomeUserDetailsServiceImplementation userDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("hashedPassword123");
        testUser.setLogin_with_google(false);
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void testLoadUserByUsername_Success() {
        // Arrange
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("testuser@example.com", result.getUsername());
        assertEquals("hashedPassword123", result.getPassword());
        verify(userRepository, times(1)).findByEmail("testuser@example.com");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void testLoadUserByUsername_NotFound() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> 
            userDetailsService.loadUserByUsername("nonexistent@example.com"));
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user logged in with Google")
    void testLoadUserByUsername_GoogleLogin() {
        // Arrange
        testUser.setLogin_with_google(true);
        when(userRepository.findByEmail("googleuser@example.com")).thenReturn(testUser);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> 
            userDetailsService.loadUserByUsername("googleuser@example.com"));
        verify(userRepository, times(1)).findByEmail("googleuser@example.com");
    }

    @Test
    @DisplayName("Should return authorities list for user")
    void testLoadUserByUsername_Authorities() {
        // Arrange
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser@example.com");

        // Assert
        assertNotNull(result);
        assertNotNull(result.getAuthorities());
        assertTrue(result.getAuthorities().isEmpty());
    }

    @Test
    @DisplayName("Should return user details with correct enabled status")
    void testLoadUserByUsername_EnabledStatus() {
        // Arrange
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser@example.com");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEnabled());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isCredentialsNonExpired());
        assertTrue(result.isAccountNonLocked());
    }
}
