package ru.yandex.ewmservice.event_request.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.ewmservice.event_request.exception.RequestIllegalStateException;
import ru.yandex.ewmservice.event_request.exception.RequestNotFoundException;
import ru.yandex.ewmservice.exception.ExceptionDto;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class RequestExceptionHandler {
    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ExceptionDto> requestNotFound(RequestNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "The required object was not found",
                HttpStatus.NOT_FOUND,
                null,
                LocalDateTime.now()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestIllegalStateException.class)
    public ResponseEntity<ExceptionDto> illegalStateError(RequestIllegalStateException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                List.of(),
                LocalDateTime.now()
        ), HttpStatus.CONFLICT);
    }
}
