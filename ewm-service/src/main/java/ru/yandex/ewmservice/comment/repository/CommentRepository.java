package ru.yandex.ewmservice.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.ewmservice.comment.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByIdAndAuthorId(Long commentId, Long userId);

    List<Comment> findByEventId(Long eventId);
}
