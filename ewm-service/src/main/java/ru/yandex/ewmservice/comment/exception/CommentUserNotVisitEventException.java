package ru.yandex.ewmservice.comment.exception;

public class CommentUserNotVisitEventException extends RuntimeException {
    public CommentUserNotVisitEventException() {
    }

    public CommentUserNotVisitEventException(Long userId, Long eventId) {
        super("User with id " + userId +
                " wasnt visit event with id " + eventId);
    }
}
