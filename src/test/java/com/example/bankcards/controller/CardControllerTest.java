package com.example.bankcards.controller;

import com.example.bankcards.dto.TransferDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JWTFilter;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    JWTFilter jwtFilter;

    @MockBean
    private CardRepository cardRepository;

    private User user;
    private Card card;

    @BeforeEach
    void setup() {

        user = User.builder().id(1L).username("user").isActive(true).build();

        card = Card.builder()
                .id(10L)
                .owner(user)
                .status(CardStatus.ACTIVE)
                .expiryDate(LocalDate.now().plusYears(1))
                .balance(new BigDecimal("100.00"))
                .number("1234567890123456")
                .build();
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCard_shouldReturnOk() throws Exception {

        doNothing().when(cardService).deleteCard(10L);

        mockMvc.perform(delete("/admin/cards/10")).andExpect(status().isOk()).andExpect(content().string("Карта удалена"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCards_shouldReturnList() throws Exception {

        Page<Card> page = new PageImpl<>(List.of(card));

        when(cardService.getAllCard(0, 10)).thenReturn(page);

        mockMvc.perform(get("/admin/cards").param("page", "0").param("size", "10")).andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void getUserCards_shouldReturnList() throws Exception {

        Page<Card> page = new PageImpl<>(List.of(card));

        when(cardService.getCardsForUser(eq(1L), eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(get("/user/cards")
                .principal(authentication()).param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void transfer_shouldReturnOk() throws Exception {

        TransferDTO dto = new TransferDTO(10L, 20L, new BigDecimal("50"));

        doNothing().when(cardService).transferBetweenCards(eq(1L), eq(10L), eq(20L), eq(new BigDecimal("50")));

        mockMvc.perform(post("/user/cards/transfer")
                .principal(authentication())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Перевод выполнен"));
    }


    @Test
    @WithMockUser
    void getBalance_shouldReturnValue() throws Exception {

        when(cardService.getCardBalance(10L)).thenReturn(new BigDecimal("100.00"));

        mockMvc.perform(get("/user/cards/10/balance")).andExpect(status().isOk()).andExpect(content().string("100.00"));
    }

    private Principal authentication() {
        return new UsernamePasswordAuthenticationToken(user, null, List.of());
    }

}
