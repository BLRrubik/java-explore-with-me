package ru.yandex.ewmservice.comment.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.ewmservice.comment.exception.CommentAuthorException;
import ru.yandex.ewmservice.comment.exception.CommentNotFoundException;
import ru.yandex.ewmservice.comment.exception.CommentUserNotVisitEventException;
import ru.yandex.ewmservice.exception.ExceptionDto;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class CommentExceptionHandler {
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ExceptionDto> commentNotFound(CommentNotFoundException e) {
        return new ResponseEntity<>(
                new ExceptionDto(
                        e.getMessage(),
                        "The required object was not found",
                        HttpStatus.NOT_FOUND,
                        List.of(),
                        LocalDateTime.now()
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentAuthorException.class)
    public ResponseEntity<ExceptionDto> authorOfCommentError(CommentAuthorException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                List.of(),
                LocalDateTime.now()
        ), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CommentUserNotVisitEventException.class)
    public ResponseEntity<ExceptionDto> userNotVisitEvent(CommentUserNotVisitEventException e) {
        return new ResponseEntity<>(new ExceptionDto(
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT,
                List.of(),
                LocalDateTime.now()
        ), HttpStatus.CONFLICT);
    }
}
