package ru.rubik.ewmservice.event.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rubik.ewmservice.event.exception.EventNotFoundException;
import ru.rubik.ewmservice.event.exception.EventStateException;
import ru.rubik.ewmservice.exception.ExceptionDto;

import java.time.LocalDateTime;

@ControllerAdvice
public class EventExceptionHandler {
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ExceptionDto> eventNotFound(EventNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventStateException.class)
    public ResponseEntity<ExceptionDto> eventStateError(EventStateException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

}
