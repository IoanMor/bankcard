package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.MapperToDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/all-user")
    public ResponseEntity<List<UserDTO>> getAllUser(@RequestParam int page, @RequestParam int size){
        List<UserDTO> userList = userService.getAllUsers(page, size).stream()
                .map(MapperToDTO::userToDTO)
                .toList();
        return ResponseEntity.ok(userList);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/{id}/setActive")
    public ResponseEntity<User> setActiveUser(@PathVariable Long id,boolean active){
        return ResponseEntity.ok(userService.setActive(id, active));
    }
    @GetMapping("/user/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok((User) authentication.getPrincipal());
    }
}

