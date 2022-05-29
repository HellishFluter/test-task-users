package ru.weber.test.rest.api.entity.exception;

public class InvalidJwtException extends RuntimeException{
    public InvalidJwtException(String jwt) {
        super("JWT is invalid'" + jwt);
    }
}