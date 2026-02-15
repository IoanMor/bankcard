package com.example.bankcards.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CustomExceptions;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CardService cardService;

    private User user;
    private Card fromCard;
    private Card toCard;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).username("testUser").password("pass").isActive(true).build();
        fromCard = Card.builder().id(10L).owner(user).balance(new BigDecimal("100.00")).status(CardStatus.ACTIVE).expiryDate(LocalDate.now().plusYears(1)).build();
        toCard = Card.builder().id(20L).owner(user).balance(new BigDecimal("50.00")).status(CardStatus.ACTIVE).expiryDate(LocalDate.now().plusYears(1)).build();
    }


    @Test
    void createCard_shouldCreateCard_whenUserExists() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.save(any(Card.class))).thenAnswer(inv -> inv.getArgument(0));

        Card result = cardService.createCard(1L);

        assertNotNull(result);
        assertEquals(user, result.getOwner());
        assertEquals(CardStatus.ACTIVE, result.getStatus());

        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void createCard_shouldThrowException_whenUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CustomExceptions.EntityNotFoundException.class, () -> cardService.createCard(1L));
        verify(cardRepository, never()).save(any());
    }



    @Test
    void transferBetweenCards_shouldTransferMoney_whenValid() {

        when(cardRepository.findById(10L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(20L)).thenReturn(Optional.of(toCard));

        cardService.transferBetweenCards(1L, 10L, 20L, new BigDecimal("30.00"));

        assertEquals(new BigDecimal("70.00"), fromCard.getBalance());
        assertEquals(new BigDecimal("80.00"), toCard.getBalance());
        verify(cardRepository).save(fromCard);
        verify(cardRepository).save(toCard);
    }

    @Test
    void transferBetweenCards_shouldThrowException_whenNotEnoughMoney() {

        when(cardRepository.findById(10L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(20L)).thenReturn(Optional.of(toCard));

        assertThrows(CustomExceptions.InvalidOperationException.class,
                () -> cardService.transferBetweenCards(1L, 10L, 20L, new BigDecimal("1000.00")));
    }

    @Test
    void transferBetweenCards_shouldThrowException_whenCardNotBelongsUser() {

        User otherUser = User.builder().id(999L).build();
        toCard.setOwner(otherUser);

        when(cardRepository.findById(10L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(20L)).thenReturn(Optional.of(toCard));

        assertThrows(CustomExceptions.CardDontBelongUserException.class,
                () -> cardService.transferBetweenCards(1L, 10L, 20L, new BigDecimal("10.00")));
    }


    @Test
    void getCardBalance_shouldReturnBalance() {
        when(cardRepository.findById(10L)).thenReturn(Optional.of(fromCard));
        BigDecimal balance = cardService.getCardBalance(10L);
        assertEquals(new BigDecimal("100.00"), balance);
    }

    @Test
    void getCardBalance_shouldThrowException_whenCardNotFound() {
        when(cardRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(CustomExceptions.EntityNotFoundException.class, () -> cardService.getCardBalance(10L));
    }

}