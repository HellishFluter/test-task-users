package ru.weber.test.rest.api.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.weber.test.rest.api.dto.ErrorResponse;
import ru.weber.test.rest.api.entity.exception.*;

import javax.validation.ConstraintViolationException;

@Log4j2
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), System.currentTimeMillis()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage());
        String message = ex.getMessage();
        if(ex.getMessage().contains("Email.message")) {
            message = "Email isn't correct";
        }
            return new ResponseEntity(
                    new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, System.currentTimeMillis()),
                    HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(ex.getMessage());
        String message = ex.getMessage();
        if(ex.getMessage().contains("users_email_key")) {
            message = "Email is used";
        }
        if(ex.getMessage().contains("phones_value_key")) {
            message = "Phone is used";
        }
        if(ex.getMessage().contains("Phone format is wrong")) {
            message = "Phone format is wrong";
        }

        return new ResponseEntity(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleLoginExistEcxeption(LoginExistException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlePhoneNotFoundException(PhoneNotFoundException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), System.currentTimeMillis()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleForbiddenForUserException(ForbiddenForUserException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), System.currentTimeMillis()),
                HttpStatus.FORBIDDEN);
    }
}
