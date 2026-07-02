package com.keyCLI01.Domain.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CryptoServiceTest {

    @Autowired
    private CryptoService cryptoService;

    @Test
    void encryptpassword() throws Exception{

        String password = "password123";
        byte[] salt = cryptoService.generateSalt();
        SecretKey key = cryptoService.deriveKey("password".toCharArray(), salt);

        EncryptedData encryptedData = cryptoService.encrypt(password, key);

        assertNotEquals(password, encryptedData.cipherText());
    }

    @Test
    void decryptpassword() throws Exception{

        String password = "password123";
        byte[] salt = cryptoService.generateSalt();
        SecretKey key = cryptoService.deriveKey("password".toCharArray(), salt);

        EncryptedData encryptedData = cryptoService.encrypt(password, key);
        String decryptedData = cryptoService.decrypt(encryptedData.cipherText(), encryptedData.iv(), key);

        assertEquals(password, decryptedData);
    }
}