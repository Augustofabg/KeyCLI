package com.keyCLI01.Domain.passwords;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table (name = "passwords")
@Entity (name = "passwords")
@Getter
@NoArgsConstructor
public class Password {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String iv;

    private String title;

    private String username;

    private String password;

    private String email;

    private String passkey;

    public Password(String iv, String title, String username, String password, String email, String passkey){

        this.iv = iv;
        this.title = title;
        this.username = username;
        this.password = password;
        this.email = email;
        this.passkey = passkey;
    }

    @Override
    public String toString() {
        return "Service: " + title + " | User: " + username + " | Email: " + email;
    }
}
