package com.example.bankcards.util;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;

public class MapperToDTO {
    public static CardDTO cardToDTO(Card card) {
        return new CardDTO(
                card.getOwner().getId(),
                card.getExpiryDate(),
                card.getStatus(),
                card.getBalance(),
                mask(card.getNumber())
        );
    }
    private static String mask(String number) {
        return "**** **** **** " + number.substring(number.length() - 4);
    }
}
