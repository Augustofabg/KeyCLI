package com.keyCLI01.Domain.passwords;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordRepository extends JpaRepository<Password, Long> {
    Optional<Password> findByTitle(String title);
}
