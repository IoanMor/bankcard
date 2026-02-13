package com.example.bankcards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {
    private Long fromCardId;
    private Long toCardId;
    private BigDecimal amount;
}

