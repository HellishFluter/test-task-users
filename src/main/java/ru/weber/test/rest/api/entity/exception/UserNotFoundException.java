package ru.weber.test.rest.api.entity.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long id) {
        super("User with Id = " + id + " not found");
    }
}
