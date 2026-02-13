package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.TransferDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardStatus;
import com.example.bankcards.util.MapperToDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/cards")
    public ResponseEntity<CardDTO> createCard(@RequestParam CardDTO cardDTO) {
        Card card = cardService.createCard(cardDTO.getOwnerId());
        return ResponseEntity.ok(MapperToDTO.cardToDTO(card));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/cards/{id}/status")
    public ResponseEntity<CardDTO> updateCardStatus(@PathVariable Long id, @RequestParam CardStatus status) {
        Card card = cardService.setStatus(id, status);
        return ResponseEntity.ok(MapperToDTO.cardToDTO(card));
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/cards/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.ok("Карта удалена");
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/cards")
    public ResponseEntity<List<CardDTO>> getAllCards() {
       List<CardDTO> dtoList = cardService.getAllCards().stream()
               .map(MapperToDTO::cardToDTO)
               .toList();
        return ResponseEntity.ok(dtoList);
    }


    @GetMapping("/user/cards")
    public ResponseEntity<List<CardDTO>> getUserCards(Authentication authentication,
                                                      @RequestParam int page,
                                                      @RequestParam int size) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        Page<Card> cards = cardService.getCardsForUser(userId, page, size);
        List<CardDTO> dtoList = cards.stream()
                .map(MapperToDTO::cardToDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/user/cards/transfer")
    public ResponseEntity<String> transfer(@RequestBody @Valid TransferDTO transferDTO,
                                           Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getId();
        cardService.transferBetweenCards(userId,
                transferDTO.getFromCardId(),
                transferDTO.getToCardId(),
                transferDTO.getAmount()
        );
        return ResponseEntity.ok("Перевод выполнен");
    }

    @GetMapping("/user/cards/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardBalance(id));
    }
}

