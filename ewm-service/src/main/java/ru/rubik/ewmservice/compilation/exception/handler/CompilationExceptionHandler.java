package ru.rubik.ewmservice.compilation.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rubik.ewmservice.compilation.exception.CompilationNotFoundException;
import ru.rubik.ewmservice.compilation.exception.CompilationUniqException;
import ru.rubik.ewmservice.exception.ExceptionDto;

import java.time.LocalDateTime;

@ControllerAdvice
public class CompilationExceptionHandler {
    @ExceptionHandler(CompilationNotFoundException.class)
    public ResponseEntity<ExceptionDto> compilationFound(CompilationNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CompilationUniqException.class)
    public ResponseEntity<ExceptionDto> compilationUniqError(CompilationUniqException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }
}
