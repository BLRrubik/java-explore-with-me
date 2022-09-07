package ru.rubik.ewmservice.user.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rubik.ewmservice.exception.ExceptionDto;
import ru.rubik.ewmservice.user.exception.UserEmailUniqException;
import ru.rubik.ewmservice.user.exception.UserNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> userNotFound(UserNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserEmailUniqException.class)
    public ResponseEntity<ExceptionDto> emailUniqError(UserEmailUniqException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }
}
