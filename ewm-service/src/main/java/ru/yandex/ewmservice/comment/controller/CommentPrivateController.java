package ru.yandex.ewmservice.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.comment.dto.CommentDto;
import ru.yandex.ewmservice.comment.requests.CommentCreateRequest;
import ru.yandex.ewmservice.comment.requests.CommentUpdateRequest;
import ru.yandex.ewmservice.comment.service.CommentService;

import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/comments/")
public class CommentPrivateController {
    private final CommentService commentService;

    @Autowired
    public CommentPrivateController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/events/{eventId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable("userId") Long userId,
                                                    @PathVariable("eventId") Long eventId,
                                                    @RequestBody CommentCreateRequest request) {
        return ResponseEntity.of(Optional.of(commentService.createComment(request, userId, eventId)));
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("userId") Long userId,
                                                    @PathVariable("eventId") Long eventId,
                                                    @RequestBody CommentUpdateRequest request) {
        return ResponseEntity.of(Optional.of(commentService.updateComment(request, userId, eventId)));
    }

    @DeleteMapping("/{commentId}/events/{eventId}")
    public void deleteComment(@PathVariable("userId") Long userId,
                              @PathVariable("eventId") Long eventId,
                              @PathVariable("commentId") Long commentId) {
        commentService.deleteCommentByUser(userId, eventId, commentId);
    }
}
