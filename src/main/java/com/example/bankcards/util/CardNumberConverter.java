package com.example.bankcards.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CardNumberConverter implements AttributeConverter<String, String> {
    private final CryptoService cryptoService;

    public CardNumberConverter(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @Override
    public String convertToDatabaseColumn(String s) {
        return cryptoService.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return cryptoService.decrypt(s);
    }
}
