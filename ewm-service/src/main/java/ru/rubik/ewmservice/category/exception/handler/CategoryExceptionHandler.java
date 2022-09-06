package ru.rubik.ewmservice.category.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.rubik.ewmservice.category.exception.CategoryNotFoundException;
import ru.rubik.ewmservice.category.exception.CategoryUniqException;
import ru.rubik.ewmservice.exception.ExceptionDto;

import java.time.LocalDateTime;

@ControllerAdvice
public class CategoryExceptionHandler {
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionDto> categoryNotFound(CategoryNotFoundException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryUniqException.class)
    public ResponseEntity<ExceptionDto> categoryUniqError(CategoryUniqException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

}
