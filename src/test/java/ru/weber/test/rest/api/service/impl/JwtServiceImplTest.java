package ru.weber.test.rest.api.service.impl;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.weber.test.rest.api.dto.UserData;
import ru.weber.test.rest.api.service.JwtService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtServiceImplTest {

    private JwtService jwtService;
    private Long USER_ID = 1L;
    private String LOGIN = "admin";

    //Token is valid until 29.05.2037
    private String TEST_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjIxMjcyMDI1ODQsInN1YiI6ImFkbWluIiwiaWQiOjF9.02ir8Yf6tn4F9LSYGXBS-uwEjGi5D3C2t90of2W83ag";


    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl(new UserData());
    }

    @Test
    void generateToken() {
       jwtService.generateToken(USER_ID, LOGIN);
    }

    @Test
    void getClaims() {
        Claims claims = jwtService.getClaims(TEST_TOKEN);
        assertEquals(Long.parseLong(claims.get("id").toString()), USER_ID);
    }
}