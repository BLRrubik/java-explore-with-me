package ru.yandex.ewmservice.comment.exception;

public class CommentUserNotVisitEventException extends RuntimeException {
    public CommentUserNotVisitEventException() {
    }

    public CommentUserNotVisitEventException(String message) {
        super(message);
    }
}
