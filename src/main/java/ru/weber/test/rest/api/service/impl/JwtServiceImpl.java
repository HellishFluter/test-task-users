package ru.weber.test.rest.api.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.weber.test.rest.api.dto.UserData;
import ru.weber.test.rest.api.entity.exception.InvalidJwtException;
import ru.weber.test.rest.api.service.JwtService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    /**
     * Time to token live in hours
     */
    private final long TTL = 1;
    private final String KEY = "test-task";

    private final UserData userData;

    @Override
    public String generateToken(Long id, String login) {
        String token = Jwts.builder()
                .setExpiration(createExpiration())
                .setSubject(login)
                .claim("id", id)
                .signWith(SignatureAlgorithm.HS256, KEY)
                .compact();
        userData.setUserId(id);
        userData.setLogin(login);
        return token;
    }

    private Date createExpiration() {
        return Date.from(LocalDateTime.now().plusHours(TTL).atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Claims getClaims(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(jwt)
                    .getBody();
            userData.setUserId(Long.parseLong(claims.get("id").toString()));
            userData.setLogin(claims.getSubject());
            return claims;
        } catch (JwtException e) {
            throw new InvalidJwtException(jwt);
        }
    }

}
