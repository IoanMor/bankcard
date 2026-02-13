package com.example.bankcards.dto;

import com.example.bankcards.entity.User;
import com.example.bankcards.util.CardStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
    private Long ownerId;
    private LocalDate expiryDate;
    private CardStatus status;
    private BigDecimal balance;
    private String numberMask;
}
