package ru.yandex.ewmservice.comment.service;

import ru.yandex.ewmservice.comment.dto.CommentDto;
import ru.yandex.ewmservice.comment.requests.CommentCreateRequest;
import ru.yandex.ewmservice.comment.requests.CommentUpdateRequest;

public interface CommentService {
    CommentDto createComment(CommentCreateRequest request, Long userId, Long eventId);

    CommentDto updateComment(CommentUpdateRequest request, Long userId, Long eventId);

    void deleteCommentByUser(Long userId, Long eventId, Long commentId);

    void deleteCommentByAdmin(Long eventId, Long commentId);
}
