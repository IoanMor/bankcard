package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String name,String password){
        User user =  User.builder().username(name).password(passwordEncoder.encode(password)).isActive(true).build();
        userRepository.save(user);
        return user;
    }
    public String deleteUser(Object t){
        User user;
        if (t instanceof String username) {
            user = Optional.ofNullable(userRepository.findUserByUsername(username))
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
        } else if (t instanceof Long id) {
            user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + id + " не найден"));
        } else {
            throw new IllegalArgumentException("Неверный тип аргумента");
        }

        userRepository.delete(user);
        return "Пользователь удален: " + t;
    }

    public User setActive(Long userId, boolean active) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + userId + " не найден"));
        user.setIsActive(active);
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return  Optional.ofNullable(userRepository.findUserByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с ID " + userId + " не найден"));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}
