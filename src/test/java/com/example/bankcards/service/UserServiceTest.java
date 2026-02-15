package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CustomExceptions;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .id(1L)
                .username("test")
                .password("encoded")
                .isActive(true)
                .build();
    }


    @Test
    void createUser_shouldCreateUser() {

        when(passwordEncoder.encode("1234")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.createUser("test", "1234");
        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("1234");
    }

    @Test
    void deleteUser_shouldDeleteById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        String result = userService.deleteUser(1L);
        assertEquals("Пользователь удален: 1", result);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(
                CustomExceptions.EntityNotFoundException.class,
                () -> userService.deleteUser(1L)
        );
    }
    @Test
    void deleteUser_shouldDeleteByUsername() {
        when(userRepository.findUserByUsername("test"))
                .thenReturn(user);
        String result = userService.deleteUser("test");
        assertEquals("Пользователь удален: test", result);
        verify(userRepository).delete(user);
    }
    @Test
    void changePassword_shouldChangePassword() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.encode("new"))
                .thenReturn("encodedNew");
        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        User result = userService.changePassword(1L, "new");

        assertNotNull(result);

        verify(passwordEncoder).encode("new");
        verify(userRepository).save(user);
    }

}