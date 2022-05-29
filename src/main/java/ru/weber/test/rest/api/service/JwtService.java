package ru.weber.test.rest.api.service;


import io.jsonwebtoken.Claims;

public interface JwtService {
    String generateToken(Long id, String login);

    Claims getClaims(String jwt);
}
