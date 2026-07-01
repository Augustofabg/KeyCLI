package com.keyCLI01.Domain.vault_meta;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "vault_meta")
@Entity(name = "vault_meta")
@Getter
@NoArgsConstructor
public class VaultMeta {

    @Id
    private String key;

    private String value;


    public VaultMeta(String key, String value){

        this.key = key;
        this.value = value;

    }
}
