package ru.yandex.ewmservice.comment.exception;

public class CommentAuthorException extends RuntimeException {
    public CommentAuthorException() {
    }

    public CommentAuthorException(Long commentId, Long userId) {
        super("Comment with id " + commentId +
                " dont have author with id " + userId);
    }
}
