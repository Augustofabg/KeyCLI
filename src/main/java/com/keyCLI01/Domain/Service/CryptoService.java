package com.keyCLI01.Domain.Service;

import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class CryptoService {

   public byte[] generateSalt(){
       byte[] salt = new byte[16];
       SecureRandom random = new SecureRandom();
       random.nextBytes(salt);
       return salt;
   };

   public SecretKey deriveKey(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
       SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
       KeySpec spec = new PBEKeySpec(password, salt, 310000, 256);
       SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
       return secret;
   };

   public EncryptedData encrypt(String text, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
       byte[] encrypted = cipher.doFinal(text.getBytes());
       String textCipher = Base64.getEncoder().encodeToString(encrypted);
       String iv64 = Base64.getEncoder().encodeToString(iv);
       return new EncryptedData(textCipher, iv64);
   };

   public String decrypt(String encrypted, String iv, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
       byte[] encryptedBytes = Base64.getDecoder().decode(encrypted);
       byte[] ivBytes = Base64.getDecoder().decode(iv);

       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
       cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivBytes));
       byte[] decrypted = cipher.doFinal(encryptedBytes);
       return new String(decrypted);

   };
}
