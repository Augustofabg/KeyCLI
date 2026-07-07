package com.keyCLI01.Domain.Service;

import com.keyCLI01.Domain.passwords.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
public class VaultCLI {

    @Autowired
    private VaultService vaultService;

    @ShellMethod(key = "create-vault", value = "Create new vault")
    public String createVault(String masterPassword) throws Exception{
        vaultService.createVault(masterPassword);

        return "Vault created successfully!";
    }

    @ShellMethod(key = "unlock-vault", value = "unlock your vault")
    public String unlockVault(String masterPassword) throws Exception{
        vaultService.unlockVault(masterPassword);

        return "Your vault is unlocked!";
    }

    @ShellMethod(key = "new-service", value = "save your service")
    public String newService(String title, String username, String password, String email, String passkey) throws Exception{
        vaultService.savePassword(title, username, password, email, passkey);

        return "New service saved";
    }

    @ShellMethod(key = "service-password", value = "show your service password")
    public String servicePassword(String title) throws Exception{
       String password = vaultService.getPassword(title);

        return password;
    }

    @ShellMethod(key = "delete-service", value = "Delete your service")
    public String deleteService(String title) throws Exception{
        vaultService.deletePassword(title);

        return "Your service is deleted";
    }

    @ShellMethod(key = "list-service", value = "List your services")
    public String listServices() throws Exception{
        List<Password> passwords = vaultService.listPassword();

        return passwords.toString();
    }
}
