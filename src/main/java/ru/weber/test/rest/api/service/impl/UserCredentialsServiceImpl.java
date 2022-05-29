package ru.weber.test.rest.api.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.weber.test.rest.api.entity.UserCredential;
import ru.weber.test.rest.api.repository.UserCredentialRepository;
import ru.weber.test.rest.api.service.UserCredentialsService;


@AllArgsConstructor
@Service
public class UserCredentialsServiceImpl implements UserCredentialsService {

    private final UserCredentialRepository userCredentialRepository;

    @Transactional
    @Override
    public UserCredential getByLogin(String login) {
        return userCredentialRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }

    @Transactional
    @Override
    public boolean existsByLogin(String login) {
        return userCredentialRepository.existsByLogin(login);
    }
}
