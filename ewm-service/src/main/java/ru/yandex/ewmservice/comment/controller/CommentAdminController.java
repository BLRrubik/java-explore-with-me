package ru.yandex.ewmservice.comment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.ewmservice.comment.service.CommentService;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/admin/comments")
public class CommentAdminController {
    private final CommentService commentService;

    @Autowired
    public CommentAdminController(CommentService commentService) {
        this.commentService = commentService;
    }

    @DeleteMapping("/{commentId}/")
    public void deleteComment(@PathVariable("commentId") @Positive Long commentId) {
        commentService.deleteCommentByAdmin(commentId);
    }
}
