package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/users")
    public ResponseEntity<String> createUser(@RequestBody @Valid AuthDTO authDTO) {
        userService.createUser(authDTO.getUsername(), authDTO.getPassword());
        return ResponseEntity.ok("Пользователь создан");
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/users/by-name/{username}")
    public ResponseEntity<String> deleteUserByName(@PathVariable String username) {
        return ResponseEntity.ok(userService.deleteUser(username));
    }

    @GetMapping("/user/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok((User) authentication.getPrincipal());
    }
}

