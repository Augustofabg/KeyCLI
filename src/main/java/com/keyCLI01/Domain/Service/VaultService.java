package com.keyCLI01.Domain.Service;

import com.keyCLI01.Domain.passwords.Password;
import com.keyCLI01.Domain.passwords.PasswordRepository;
import com.keyCLI01.Domain.vault_meta.VaultMeta;
import com.keyCLI01.Domain.vault_meta.VaultMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;

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
            throw new IllegalAccessException("Incorrect password!");
        }

    }

    public void savePassword(String title, String username, String password, String email, String passkey) throws Exception{
        if (sessionKey == null){
            throw new Exception("locked!");
        } else {
           EncryptedData encryptPass = cryptoService.encrypt(password, sessionKey);
           Password service = new Password(encryptPass.iv(), title, username, encryptPass.cipherText(), email, passkey);
           passwordRepository.save(service);
        }
    }

   public String getPassword(String title) throws Exception{
        if (sessionKey == null){
            throw new Exception("locked!");
        }

        Password vaultFind = passwordRepository.findByTitle(title).orElseThrow();
        String decryptedPass = cryptoService.decrypt(vaultFind.getPassword(), vaultFind.getIv(), sessionKey);

        return decryptedPass;

   }

   public void deletePassword(String title) throws Exception{
        if (sessionKey == null){
            throw new Exception("locked!");
        }

        Password vaultDelete = passwordRepository.findByTitle(title).orElseThrow();
        passwordRepository.delete(vaultDelete);
   }

   public List<Password> listPassword() throws Exception{
       if (sessionKey == null){
           throw new Exception("locked!");
       }

       List<Password> vaulList = passwordRepository.findAll();
       return vaulList;

   }

}
