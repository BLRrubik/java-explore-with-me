package ru.yandex.ewmservice.event.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.ewmservice.event.exception.EventNotFoundException;
import ru.yandex.ewmservice.event.exception.EventStateException;
import ru.yandex.ewmservice.exception.ExceptionDto;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class EventExceptionHandler {
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ExceptionDto> eventNotFound(EventNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "The required object was not found",
                HttpStatus.NOT_FOUND,
                null,
                LocalDateTime.now()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventStateException.class)
    public ResponseEntity<ExceptionDto> eventStateError(EventStateException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                List.of(),
                LocalDateTime.now()
        ), HttpStatus.CONFLICT);
    }

}
