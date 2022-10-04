package ru.yandex.ewmservice.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
    }

    public CommentNotFoundException(Long commentId) {
        super("Comment with id " + commentId + " is not found");
    }
}
