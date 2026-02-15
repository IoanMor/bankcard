package com.example.bankcards.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.example.bankcards.dto.AuthDTO;
import com.example.bankcards.dto.TransferDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JWTFilter;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.CardStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    JWTFilter jwtFilter;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {

        user = User.builder().id(1L).username("user").isActive(true).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_shouldReturnOk() throws Exception {

        AuthDTO dto = new AuthDTO("user", "pass");

        when(userService.createUser("user", "pass"))
                .thenReturn(user);

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь создан"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_shouldReturnOk() throws Exception {

        when(userService.deleteUser(1L)).thenReturn("Пользователь удален: 1");

        mockMvc.perform(delete("/admin/users/1")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_shouldReturnList() throws Exception {

        Page<User> page = new PageImpl<>(List.of(user));

        when(userService.getAllUsers(0, 10))
                .thenReturn(page);

        mockMvc.perform(get("/admin/users/all-user").param("page", "0").param("size", "10")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void setActive_shouldReturnUser() throws Exception {

        when(userService.setActive(1L, true))
                .thenReturn(user);

        mockMvc.perform(get("/admin/users/1/setActive").param("active", "true"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void getCurrentUser_shouldReturnUser() throws Exception {

        mockMvc.perform(get("/user/me")
                        .principal(
                                new UsernamePasswordAuthenticationToken(user, null, List.of())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }

}
