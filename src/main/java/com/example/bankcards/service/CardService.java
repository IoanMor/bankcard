package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Card createCard(Long userId, String cardNumber, BigDecimal initialBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        Card card = Card.builder()
                .number(cardNumber)
                .balance(initialBalance)
                .status(CardStatus.ACTIVE)
                .owner(user)
                .build();

        return cardRepository.save(card);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Card setStatus(Long cardId, CardStatus cardStatus) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Карта не найдена"));
        card.setStatus(cardStatus);
        return cardRepository.save(card);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Карта не найдена"));
        cardRepository.delete(card);
    }


    public Page<Card> getCardsForUser(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        return cardRepository.findCardsByOwnerId(userId, PageRequest.of(page, size));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }


    public void transferBetweenCards(Long fromCardId, Long toCardId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть положительной");
        }

        Card fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new IllegalArgumentException("Исходная карта не найдена"));
        Card toCard = cardRepository.findById(toCardId)
                .orElseThrow(() -> new IllegalArgumentException("Карта назначения не найдена"));

        if (fromCard.getOwner().getId()!=(toCard.getOwner().getId())) {
            throw new IllegalArgumentException("Перевод возможен только между своими картами");
        }

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на карте");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }


    public BigDecimal getCardBalance(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Карта не найдена"));
        return card.getBalance();
    }
}
