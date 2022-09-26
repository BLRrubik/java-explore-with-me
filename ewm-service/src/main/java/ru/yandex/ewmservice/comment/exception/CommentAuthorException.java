package ru.yandex.ewmservice.comment.exception;

public class CommentAuthorException extends RuntimeException {
    public CommentAuthorException() {
    }

    public CommentAuthorException(String message) {
        super(message);
    }
}
