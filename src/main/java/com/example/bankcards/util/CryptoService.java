package com.example.bankcards.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Service
public class CryptoService {


    private final SecretKeySpec key;

    public CryptoService(@Value("${spring.security.secretKeyCard}") String secretKey) {
        this.key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public String encrypt(String value){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка шифрования " + e.getMessage());
        }
    }

    public String decrypt(String value){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(value)));
        }catch (Exception e){
            throw new RuntimeException("Ошибка де-шифрования " + e.getMessage());
        }
    }
}
