package com.keyCLI01.Domain.Service;

import com.keyCLI01.Domain.passwords.PasswordRepository;
import com.keyCLI01.Domain.vault_meta.VaultMeta;
import com.keyCLI01.Domain.vault_meta.VaultMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Service
public class VaultService{

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private VaultMetaRepository vaultMetaRepository;

    private SecretKey sessionKey;

    public void createVault(String masterPassword) throws Exception{
      byte[] salt = cryptoService.generateSalt();
      String salt64 = Base64.getEncoder().encodeToString(salt);
      SecretKey deriveKey = cryptoService.deriveKey(masterPassword.toCharArray(), salt);

      EncryptedData encryptedData = cryptoService.encrypt("vault_ok", deriveKey);

      vaultMetaRepository.save(new VaultMeta("salt", salt64));
      vaultMetaRepository.save(new VaultMeta("verifier", encryptedData.cipherText()));
      vaultMetaRepository.save(new VaultMeta("iv", encryptedData.iv()));

    }

    public void unlockVault(String masterPassword) throws Exception{
        VaultMeta saltMeta = vaultMetaRepository.findById("salt").orElseThrow();
        byte[] salt = Base64.getDecoder().decode(saltMeta.getValue());

        SecretKey deriveKey = cryptoService.deriveKey(masterPassword.toCharArray(), salt);

        VaultMeta verifierMeta = vaultMetaRepository.findById("verifier").orElseThrow();
        VaultMeta ivMeta = vaultMetaRepository.findById("iv").orElseThrow();

        String decrypt = cryptoService.decrypt(verifierMeta.getValue(), ivMeta.getValue(), deriveKey);

        if ("vault_ok".equals(decrypt)){
           sessionKey = deriveKey;
        } else {
            throw new IllegalAccessException("Senha incorreta!");
        }

    }



}
