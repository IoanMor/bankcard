package com.example.bankcards.util;

import com.example.bankcards.dto.AuthDTO;
import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;

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
    public static UserDTO userToDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getIsActive(),
                user.getRoles()
        );
    }
    private static String mask(String number) {
        return "**** **** **** " + number.substring(number.length() - 4);
    }
}
