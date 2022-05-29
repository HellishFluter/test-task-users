package ru.weber.test.rest.api.service;

import ru.weber.test.rest.api.entity.UserCredential;

public interface UserCredentialsService {
    UserCredential getByLogin(String login);
    boolean existsByLogin(String login);
}
