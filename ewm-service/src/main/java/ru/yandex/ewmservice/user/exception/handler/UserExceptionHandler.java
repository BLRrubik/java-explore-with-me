package ru.yandex.ewmservice.user.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.ewmservice.exception.ExceptionDto;
import ru.yandex.ewmservice.user.exception.UserEmailUniqException;
import ru.yandex.ewmservice.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> userNotFound(UserNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "The required object was not found",
                HttpStatus.NOT_FOUND,
                null,
                LocalDateTime.now()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserEmailUniqException.class)
    public ResponseEntity<ExceptionDto> emailUniqError(UserEmailUniqException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                List.of(),
                LocalDateTime.now()), HttpStatus.CONFLICT);
    }
}
