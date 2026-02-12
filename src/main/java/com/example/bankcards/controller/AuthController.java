package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthDTO;
import com.example.bankcards.security.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        String token = authService.authenticate(authDTO);
        return ResponseEntity.ok(Map.of("TOKEN", token));
    }
}
