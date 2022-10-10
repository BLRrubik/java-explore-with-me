package ru.yandex.ewmservice.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.comment.dto.CommentDto;
import ru.yandex.ewmservice.comment.requests.CommentCreateRequest;
import ru.yandex.ewmservice.comment.requests.CommentUpdateRequest;
import ru.yandex.ewmservice.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
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
    public ResponseEntity<CommentDto> createComment(@PathVariable("userId") @Positive Long userId,
                                                    @PathVariable("eventId") @Positive Long eventId,
                                                    @RequestBody CommentCreateRequest request) {
        return ResponseEntity.of(Optional.of(commentService.createComment(request, userId, eventId)));
    }

    @PutMapping()
    public ResponseEntity<CommentDto> updateComment(@PathVariable("userId") @Positive Long userId,
                                                    @RequestBody @Valid CommentUpdateRequest request) {
        return ResponseEntity.of(Optional.of(commentService.updateComment(request, userId)));
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable("userId") @Positive Long userId,
                              @PathVariable("commentId") @Positive Long commentId) {
        commentService.deleteCommentByUser(userId, commentId);
    }
}
