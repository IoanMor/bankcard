package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthDTO;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.AuthService;
import com.example.bankcards.security.JWTFilter;
import com.example.bankcards.security.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    JWTFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;

    @Test
    void login_shouldReturnToken() throws Exception {

        AuthDTO dto = new AuthDTO("user", "password");

        when(authService.authenticate(any(AuthDTO.class)))
                .thenReturn("jwt-token");

        mockMvc.perform(post("/login/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.TOKEN").value("jwt-token"));
    }
}