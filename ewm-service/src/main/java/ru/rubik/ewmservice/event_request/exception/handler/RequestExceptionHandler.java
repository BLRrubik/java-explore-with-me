package ru.rubik.ewmservice.event_request.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rubik.ewmservice.event_request.exception.RequestIllegalStateException;
import ru.rubik.ewmservice.event_request.exception.RequestNotFoundException;
import ru.rubik.ewmservice.exception.ExceptionDto;

import java.time.LocalDateTime;

@ControllerAdvice
public class RequestExceptionHandler {
    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ExceptionDto> requestNotFound(RequestNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestIllegalStateException.class)
    public ResponseEntity<ExceptionDto> requestNotFound(RequestIllegalStateException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }
}
