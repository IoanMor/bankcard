package com.example.bankcards.util;


import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.random.RandomGenerator;


public class GenerateCardNumber {
    public static String generate(){
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i <= 3; i++) {
            cardNumber.append(random.nextInt(1000,9000));
            if (i!=3) {
                cardNumber.append("-");
            }
        }
        return cardNumber.toString();
    }


}
