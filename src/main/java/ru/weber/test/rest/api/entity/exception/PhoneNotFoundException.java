package ru.weber.test.rest.api.entity.exception;

public class PhoneNotFoundException extends RuntimeException{

    public PhoneNotFoundException(Long phoneId) {
        super("Phone with Id = " + phoneId + " not found");
    }
}