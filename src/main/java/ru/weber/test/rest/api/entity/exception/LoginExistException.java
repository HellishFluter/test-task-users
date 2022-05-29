package ru.weber.test.rest.api.entity.exception;

public class LoginExistException extends RuntimeException{
    public LoginExistException(String login) {
        super("Login '" + login + "' already exists");
    }
}