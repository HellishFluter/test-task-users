package ru.weber.test.rest.api.entity.exception;

public class ForbiddenForUserException extends RuntimeException{

    public ForbiddenForUserException(Long userId) {
        super("Action is forbidden for user id: " + userId);
    }
}
