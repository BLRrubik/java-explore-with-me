package ru.yandex.ewmservice.category.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.ewmservice.category.exception.CategoryNotFoundException;
import ru.yandex.ewmservice.category.exception.CategoryUniqException;
import ru.yandex.ewmservice.exception.ExceptionDto;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class CategoryExceptionHandler {
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionDto> categoryNotFound(CategoryNotFoundException e) {
        return new ResponseEntity<>(
                new ExceptionDto(
                        e.getMessage(),
                        "The required object was not found",
                        HttpStatus.NOT_FOUND,
                        List.of(),
                        LocalDateTime.now()
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryUniqException.class)
    public ResponseEntity<ExceptionDto> categoryUniqError(CategoryUniqException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                List.of(),
                LocalDateTime.now()
        ), HttpStatus.CONFLICT);
    }

}
