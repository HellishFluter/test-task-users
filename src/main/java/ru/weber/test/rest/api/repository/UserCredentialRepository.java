package ru.weber.test.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.weber.test.rest.api.entity.UserCredential;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findByLogin(String login);

    Boolean existsByLogin(String login);

    void deleteByUserId(Long id);
}
