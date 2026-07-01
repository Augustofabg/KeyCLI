package com.keyCLI01.Domain.passwords;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        this.username = this.username;
        this.password = password;
        this.email = email;
        this.passkey = passkey;

    }
}
