package com.example.bankcards.security;

import com.example.bankcards.dto.AuthDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
private final AuthenticationManager authenticationManager;
private final UserDetailsService userDetailsService;
private final JWTUtil jwt;

public String authenticate (AuthDTO authDTO){
    System.err.println("USERNAME: " + authDTO.getUsername());
    System.err.println("PASSWORD: " + authDTO.getPassword());
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getUsername(),authDTO.getPassword()));
    return jwt.generateToken(userDetailsService.loadUserByUsername(authDTO.getUsername()));
}
}