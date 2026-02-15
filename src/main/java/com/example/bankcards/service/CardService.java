package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CustomExceptions;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardStatus;
import com.example.bankcards.util.GenerateCardNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public Card createCard(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException("Пользователь с id " + ownerId + " не найден"));
        Card card = new Card();
        card.setOwner(owner);
        card.setStatus(CardStatus.ACTIVE);
        return cardRepository.save(card);
    }

    public Card setStatus(Long cardId, CardStatus status) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException("Карта с id " + cardId + " не найдена"));
        card.setStatus(status);
        return cardRepository.save(card);
    }

    public void deleteCard(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new CustomExceptions.EntityNotFoundException("Карта с id " + cardId + " не найдена");
        }
        cardRepository.deleteById(cardId);
    }

    public Page<Card> getCardsForUser(Long userId, int page, int size) {
        if (!userRepository.existsById(userId)) {
            throw new CustomExceptions.EntityNotFoundException("Пользователь с id " + userId + " не найден");
        }
        return cardRepository.findCardsByOwnerId(userId, PageRequest.of(page, size));
    }

    public void transferBetweenCards(Long userId, Long fromCardId, Long toCardId, BigDecimal amount) {
        Card fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException("Карта отправителя не найдена"));
        Card toCard = cardRepository.findById(toCardId)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException("Карта получателя не найдена"));

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new CustomExceptions.InvalidOperationException("Недостаточно средств на карте");
        }

        if (Objects.equals(fromCard.getOwner().getId(),userId) && Objects.equals(toCard.getOwner().getId(),userId)) {
            fromCard.setBalance(fromCard.getBalance().subtract(amount));
            toCard.setBalance(toCard.getBalance().add(amount));
            cardRepository.save(fromCard);
            cardRepository.save(toCard);
        } else throw new CustomExceptions.CardDontBelongUserException("Одна из карт не принадлежит пользователю");
    }

    public Page<Card> getAllCard(int page, int size){
        return cardRepository.findAll(PageRequest.of(page, size));
    }

    public BigDecimal getCardBalance(Long cardId) {
        return cardRepository.findById(cardId)
                .map(Card::getBalance)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException("Карта с id " + cardId + " не найдена"));
    }
}

